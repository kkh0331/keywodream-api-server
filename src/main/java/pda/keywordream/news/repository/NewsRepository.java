package pda.keywordream.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pda.keywordream.news.entity.News;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findAllByIdInOrderByCreatedAtDesc(List<Long> newIds);

}
