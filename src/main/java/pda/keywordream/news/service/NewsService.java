package pda.keywordream.news.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pda.keywordream.client.FlaskApi;
import pda.keywordream.client.dto.flask.NewsSentimentAnalysisRes;
import pda.keywordream.news.dto.NewsKeywordResDto;
import pda.keywordream.news.dto.NewsDetailResDto;
import pda.keywordream.news.dto.NewsResDto;
import pda.keywordream.news.dto.NewsSentimetAnalysisResDto;
import pda.keywordream.news.entity.News;
import pda.keywordream.news.entity.NewsKeyword;
import pda.keywordream.news.entity.NewsStock;
import pda.keywordream.news.repository.NewsKeywordRepository;
import pda.keywordream.news.repository.NewsRepository;
import pda.keywordream.news.repository.NewsStockRepository;
import pda.keywordream.stock.entity.Stock;
import pda.keywordream.stock.repository.StockRepository;
import pda.keywordream.utils.ApiUtils.ApiResult;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private final FlaskApi flaskApi;

    public NewsDetailResDto getNews(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NoSuchElementException("해당 뉴스가 존재하지 않습니다."));
        String newsDate = changeNewsDateFormat(news.getCreatedAt());
        return NewsDetailResDto.builder()
                .title(news.getTitle())
                .newsDate(newsDate)
                .press(news.getPress())
                .content(news.getContent()) //TODO content 부분에 음양처리 추가
                .isGood(news.getIsGood())
                .build();
    }

    public List<NewsKeywordResDto> getNewsKeywords(String stockCode, Integer count){
        List<Long> newsIdsByStockCode = getNewsIdsByStockCode(stockCode);
        List<NewsKeyword> newsKeywords = newsKeywordRepository.findAllByNewsIdIn(newsIdsByStockCode);
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

    public List<NewsResDto> getNewsList(String stockCode) {
        List<Long> newsIdsByStockCode = getNewsIdsByStockCode(stockCode);
        List<News> newsList = newsRepository.findAllByIdInOrderByCreatedAtDesc(newsIdsByStockCode);
        return newsList.stream()
                .map(this::toNewsResDto)
                .toList();
    }

    public List<NewsResDto> getNewsList(String stockCode, String keyword) {
        List<Long> newsIdsByStockCode = getNewsIdsByStockCode(stockCode);
        List<News> newsList = newsRepository.findAllByIdInOrderByCreatedAtDesc(newsIdsByStockCode);
        return newsList.stream()
                .filter(news -> isKeywordInNews(news, keyword))
                .map(this::toNewsResDto)
                .toList();
    }

    private NewsResDto toNewsResDto(News news){
        String newsDate = changeNewsListDateFormat(news.getCreatedAt());
        return NewsResDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .press(news.getPress())
                .newsDate(newsDate)
                .imgUrl(news.getImgUrl())
                .build();
    }

    private Boolean isKeywordInNews(News news, String keyword){
        return news.getContent().contains(keyword);
    }

    private List<Long> getNewsIdsByStockCode(String stockCode){
        Integer recentNewsCount = getStockRecentNewsCount(stockCode);
        List<NewsStock> newsStocks = newsStockRepository.findAllByStockCodeOrderByIdDesc(stockCode).stream()
                .limit(recentNewsCount)
                .toList();
        return newsStocks.stream().map(NewsStock::getNewsId).toList();
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

    private String changeNewsListDateFormat(Timestamp timestamp){
        Date currentDate = new Date();
        Date newsDate = new Date(timestamp.getTime());
        long timeDiff = currentDate.getTime() - newsDate.getTime();

        long seconds = timeDiff/1000;
        long minutes = seconds/60;
        long hours = minutes/60;
        long days = hours/24;

        if(days > 0){
            return days + "일 전";
        } else if(hours > 0){
            return hours + "시간 전";
        } else {
            return minutes + "분 전";
        }
    }

    public NewsSentimetAnalysisResDto getNewsSentimentAnalysisResult(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NoSuchElementException("해당 뉴스가 존재하지 않습니다."));
        if(news.getIsGood() != null){
            return new NewsSentimetAnalysisResDto(news.getIsGood());
        }
        NewsSentimentAnalysisRes analyzedNewsSentiment = flaskApi.analyzeNewsSentiment(newsId);
        if(analyzedNewsSentiment.getSuccess()){
            return new NewsSentimetAnalysisResDto(analyzedNewsSentiment.getResponse().getIsGood());
        }
        throw new RuntimeException("뉴스 감정 분석이 제대로 이루어지지 않았습니다.");
    }

}
