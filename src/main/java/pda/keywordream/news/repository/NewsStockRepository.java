package pda.keywordream.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pda.keywordream.news.entity.NewsStock;

import java.util.List;

public interface NewsStockRepository extends JpaRepository<NewsStock, Long> {

    List<NewsStock> findAllByStockCodeOrderByIdDesc(String stockCode);

}
