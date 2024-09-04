package pda.keywordream.stock.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pda.keywordream.client.KoInvSecApi;
import pda.keywordream.heart.entity.HeartStock;
import pda.keywordream.heart.repository.HeartStockRepository;
import pda.keywordream.stock.dto.*;
import pda.keywordream.client.dto.koinvsec.StockDailyPrice;
import pda.keywordream.client.dto.koinvsec.StockDailyPriceRes;
import pda.keywordream.client.dto.koinvsec.StockPriceRes;
import pda.keywordream.stock.entity.Stock;
import pda.keywordream.stock.repository.StockRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final HeartStockRepository heartStockRepository;

    private final KoInvSecApi koInvSecApi;

    public GetStocksResDto getStocks(GetStocksReqDto reqDto, Long userId) {
        Page<Stock> page = getStocksByCondition(reqDto);
        if(page.isEmpty()){
            throw new NoSuchElementException("해당 조건에 맞는 Stocks이 서버에 없습니다.");
        }
        PageNation pageNation = PageNation.builder()
                .currentPage(reqDto.getPage())
                .totalCount(page.getTotalElements())
                .build();
        List<String> heartedStockCodes = heartStockRepository.findAllByUserId(userId).stream()
                .map(HeartStock::getStockCode).toList();
        List<StockResDto> stocks = page.stream()
                .map(stock -> StockResDto.builder()
                        .code(stock.getCode())
                        .name(stock.getName())
                        .isHearted(heartedStockCodes.contains(stock.getCode()))
                        .build())
                .toList();
        return GetStocksResDto.builder()
                .pageNation(pageNation)
                .stocks(stocks)
                .build();
    }

    public Page<Stock> getStocksByCondition(GetStocksReqDto reqDto){
        int page = reqDto.getPage();
        int limit = reqDto.getLimit();
        String code = reqDto.getCode();
        String name = reqDto.getName();
        Pageable pageable = PageRequest.of(page-1, limit);
        if(code != null){
            return stockRepository.findAllByCodeContaining(code, pageable);
        } else if(name != null){
            return stockRepository.findAllByNameContaining(name, pageable);
        }
        return stockRepository.findAll(pageable);
    }

    public GetStockResDto getStock(String stockCode) {
        Stock stock = stockRepository.findByCode(stockCode)
                .orElseThrow(() -> new NoSuchElementException("해당 주식이 존재하지 않습니다."));
        StockPriceRes stockPriceRes = koInvSecApi.fetchStockPrice(stockCode);
        return GetStockResDto.builder()
                .code(stockCode)
                .name(stock.getName())
                .price(stockPriceRes.getOutput().getPrice())
                .ratio(stockPriceRes.getOutput().getRatio())
                .build();
    }

    public List<StockDailyPriceResDto> getStockDailyPrices(String stockCode, Date startDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        StockDailyPriceRes stockDailyPriceRes = koInvSecApi.fetchStockDailyPrice(stockCode, sdf.format(startDate), sdf.format(endDate));
        return stockDailyPriceRes.getOutput2().stream()
                .map(StockDailyPrice::toStockDailyPriceResDto)
                .toList();
    }

    public void checkStock(String stockCode){
        stockRepository.findByCode(stockCode)
                .orElseThrow(() -> new NoSuchElementException("해당 주식이 존재하지 않습니다."));
    }

}
