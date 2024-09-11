package pda.keywordream.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pda.keywordream.news.entity.News;

public interface NewsRepository extends JpaRepository<News, Long> {
}
