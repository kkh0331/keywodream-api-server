package pda.keywordream.heart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pda.keywordream.heart.entity.HeartStock;


public interface HeartStockRepository extends JpaRepository<HeartStock, Long> {
}
