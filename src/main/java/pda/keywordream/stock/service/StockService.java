package pda.keywordream.stock.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pda.keywordream.stock.client.KoInvSecClient;
import pda.keywordream.stock.dto.*;
import pda.keywordream.stock.dto.api.StockPriceApi;
import pda.keywordream.stock.entity.Stock;
import pda.keywordream.stock.repository.StockRepository;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final KoInvSecClient koInvSecClient;

    public GetStocksResDto getStocks(GetStocksReqDto reqDto) {
        Page<Stock> page = getStocksByCondition(reqDto);
        if(page.isEmpty()){
            throw new NoSuchElementException("해당 조건에 맞는 Stocks이 서버에 없습니다.");
        }
        PageNation pageNation = PageNation.builder()
                .currentPage(reqDto.getPage())
                .totalCount(page.getTotalElements())
                .build();
        List<StockResDto> stocks = page.stream().map(Stock::toStockResDto).toList();
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
        StockPriceApi stockPriceApi = koInvSecClient.fetchStockPrice(stockCode);
        return GetStockResDto.builder()
                .code(stockCode)
                .name(stock.getName())
                .price(stockPriceApi.getOutput().getPrice())
                .ratio(stockPriceApi.getOutput().getRatio())
                .build();
    }
}
