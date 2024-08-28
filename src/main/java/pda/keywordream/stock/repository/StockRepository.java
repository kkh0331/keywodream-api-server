package pda.keywordream.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pda.keywordream.stock.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, String> {
}
