package pda.keywordream.news.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pda.keywordream.news.dto.NewsKeywordResDto;
import pda.keywordream.news.dto.NewsResDto;
import pda.keywordream.news.entity.News;
import pda.keywordream.news.entity.NewsKeyword;
import pda.keywordream.news.entity.NewsStock;
import pda.keywordream.news.repository.NewsKeywordRepository;
import pda.keywordream.news.repository.NewsRepository;
import pda.keywordream.news.repository.NewsStockRepository;
import pda.keywordream.stock.entity.Stock;
import pda.keywordream.stock.repository.StockRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsStockRepository newsStockRepository;
    private final NewsKeywordRepository newsKeywordRepository;
    private final StockRepository stockRepository;

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

    public List<NewsKeywordResDto> getNewsKeywords(String stockCode, Integer count){
        Integer recentNewsCount = getStockRecentNewsCount(stockCode);
        List<NewsStock> newsStocks = newsStockRepository.findAllByStockCodeOrderByIdDesc(stockCode).stream()
                .limit(recentNewsCount)
                .toList();
        List<Long> newsIds = newsStocks.stream().map(NewsStock::getNewsId).toList();
        List<NewsKeyword> newsKeywords = newsKeywordRepository.findAllByNewsIdIn(newsIds);
        Map<String, Integer> keywordCountMap = newsKeywords.stream().collect(Collectors.groupingBy(
                NewsKeyword::getKeyword,
                Collectors.summingInt(NewsKeyword::getCount)
        ));
        return keywordCountMap.entrySet().stream()
                .map(entry -> new NewsKeywordResDto(entry.getKey(), entry.getValue()))
                .sorted((dto1, dto2) -> dto2.getCount().compareTo(dto1.getCount()))
                .limit(count)
                .toList();
    }

    private Integer getStockRecentNewsCount(String stockCode){
        Stock stock = stockRepository.findByCode(stockCode)
                .orElseThrow(() -> new NoSuchElementException("해당 주식이 존재하지 않습니다."));
        return stock.getRecentNewsCount();
    }

    private String changeNewsDateFormat(Timestamp timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
        return sdf.format(timestamp);
    }

}
