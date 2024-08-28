package pda.keywordream.stock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pda.keywordream.stock.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, String> {

    Page<Stock> findAllByCodeContaining(String code, Pageable pageable);
    Page<Stock> findAllByNameContaining(String name, Pageable pageable);
    Page<Stock> findAll(Pageable pageable);

}
