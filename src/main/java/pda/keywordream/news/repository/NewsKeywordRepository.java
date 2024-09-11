package pda.keywordream.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pda.keywordream.news.entity.NewsKeyword;

import java.util.List;

public interface NewsKeywordRepository extends JpaRepository<NewsKeyword, Long> {

    List<NewsKeyword> findAllByNewsIdIn(List<Long> newsIds);

}
