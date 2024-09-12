package pda.keywordream.news;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pda.keywordream.news.dto.NewsDetailResDto;
import pda.keywordream.news.dto.NewsResDto;
import pda.keywordream.news.dto.NewsSentimetAnalysisResDto;
import pda.keywordream.news.service.NewsService;
import pda.keywordream.stock.service.StockService;
import pda.keywordream.utils.ApiUtils;
import pda.keywordream.utils.ApiUtils.ApiResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks/{stockCode}/news")
public class NewsController {

    private final NewsService newsService;
    private final StockService stockService;

    @GetMapping("/{newsId}")
    public ResponseEntity<ApiResult<NewsDetailResDto>> getNews(
            @PathVariable String stockCode,
            @PathVariable Long newsId
    ){
        stockService.checkStock(stockCode);
        NewsDetailResDto newsDetailResDto = newsService.getNews(newsId);
        return ResponseEntity.ok(ApiUtils.success(newsDetailResDto));
    }

    @GetMapping("")
    public ResponseEntity<ApiResult<List<NewsResDto>>> getNewsList(
            @PathVariable String stockCode,
            @RequestParam(required = false) String keyword
    ){
        stockService.checkStock(stockCode);
        List<NewsResDto> newsResDtos;
        if(keyword == null){
            newsResDtos = newsService.getNewsList(stockCode);
        } else {
            newsResDtos = newsService.getNewsList(stockCode, keyword);
        }
        return ResponseEntity.ok(ApiUtils.success(newsResDtos));
    }

    @GetMapping("/{newsId}/sentiment-analysis")
    public ResponseEntity<ApiResult<NewsSentimetAnalysisResDto>> getNewsSentimentAnalysisResult(
            @PathVariable String stockCode,
            @PathVariable Long newsId
    ){
        stockService.checkStock(stockCode);
        NewsSentimetAnalysisResDto newsSentimetAnalysisResDto = newsService.getNewsSentimentAnalysisResult(newsId);
        return ResponseEntity.ok(ApiUtils.success(newsSentimetAnalysisResDto));
    }


}
