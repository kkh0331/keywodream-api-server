package pda.keywordream.heart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pda.keywordream.heart.entity.HeartStock;

import java.util.List;
import java.util.Optional;


public interface HeartStockRepository extends JpaRepository<HeartStock, Long> {

    List<HeartStock> findAllByUserId(Long userId);
    Optional<HeartStock> findByUserIdAndStockCode(Long userId, String StockCode);

}
