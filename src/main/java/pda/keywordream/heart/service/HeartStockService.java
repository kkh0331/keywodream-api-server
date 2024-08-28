package pda.keywordream.heart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pda.keywordream.heart.entity.HeartStock;
import pda.keywordream.heart.repository.HeartStockRepository;
import pda.keywordream.stock.repository.StockRepository;
import pda.keywordream.utils.exceptions.NoSaveElementException;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class HeartStockService {

    private final HeartStockRepository heartStockRepository;
    private final StockRepository stockRepository;

    public void registerHeartStock(Long userId, String stockCode) {
        stockRepository.findByCode(stockCode)
                .orElseThrow(() -> new NoSuchElementException("해당 주식이 존재하지 않습니다."));
        HeartStock heartStock = HeartStock.builder()
                .userId(userId)
                .stockCode(stockCode)
                .build();
        HeartStock savedHeartStock = heartStockRepository.save(heartStock);
        if(savedHeartStock.getId() == null){
            throw new NoSaveElementException("찜 목록 추가에 실패했습니다.");
        }
    }
}
