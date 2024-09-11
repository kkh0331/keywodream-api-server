package pda.keywordream.news.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pda.keywordream.news.dto.NewsResDto;
import pda.keywordream.news.entity.News;
import pda.keywordream.news.repository.NewsRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;

@Transactional
@RequiredArgsConstructor
@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsResDto getNews(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NoSuchElementException("해당 뉴스가 존재하지 않습니다."));
        String newsDate = changeNewsDateFormat(news.getCreatedAt());
        return NewsResDto.builder()
                .title(news.getTitle())
                .newsDate(newsDate)
                .press(news.getPress())
                .content(news.getContent()) //TODO content 부분에 음양처리 추가
                .isGood(news.getIsGood())
                .build();
    }

    private String changeNewsDateFormat(Timestamp timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
        return sdf.format(timestamp);
    }

}
