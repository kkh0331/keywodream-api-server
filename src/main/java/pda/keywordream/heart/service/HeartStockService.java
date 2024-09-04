package pda.keywordream.heart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pda.keywordream.heart.dto.HeartStockResDto;
import pda.keywordream.heart.entity.HeartStock;
import pda.keywordream.heart.repository.HeartStockRepository;
import pda.keywordream.stock.entity.Stock;
import pda.keywordream.stock.repository.StockRepository;
import pda.keywordream.utils.exceptions.NoSaveElementException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class HeartStockService {

    private final HeartStockRepository heartStockRepository;
    private final StockRepository stockRepository;

    public void registerHeartStock(Long userId, String stockCode) {
        HeartStock heartStock = HeartStock.builder()
                .userId(userId)
                .stockCode(stockCode)
                .build();
        HeartStock savedHeartStock = heartStockRepository.save(heartStock);
        if(savedHeartStock.getId() == null){
            throw new NoSaveElementException("찜 목록 추가에 실패했습니다.");
        }
    }

    public List<HeartStockResDto> getHearStocks(Long userId) {
        List<HeartStock> heartStocks = heartStockRepository.findAllByUserId(userId);
        return heartStocks.stream()
                .map(heartStock -> {
                    Stock stock = stockRepository.findByCode(heartStock.getStockCode()).orElse(null);
                    return HeartStockResDto.builder()
                            .id(heartStock.getId())
                            .stock(stock)
                            .build();
                })
                .filter(heartStockResDto -> heartStockResDto.getStock() != null)
                .toList();
    }

    public void deleteHeartStock(Long userId, String stockCode) {
        HeartStock heartStock = heartStockRepository.findByUserIdAndStockCode(userId, stockCode)
                .orElseThrow(() -> new NoSuchElementException("찜 목록에 해당 주식이 없습니다."));
        heartStockRepository.deleteById(heartStock.getId());
    }

    public void checkHeartStock(Long userId, String stockCode) {
        Optional<HeartStock> hearStock =  heartStockRepository.findByUserIdAndStockCode(userId, stockCode);
        if(hearStock.isPresent()){
            throw new RuntimeException("찜 목록에서 해당 주식 취소가 실패했습니다.");
        }
    }
}
