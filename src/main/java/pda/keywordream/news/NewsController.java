package pda.keywordream.news;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pda.keywordream.news.dto.NewsResDto;
import pda.keywordream.news.service.NewsService;
import pda.keywordream.stock.service.StockService;
import pda.keywordream.utils.ApiUtils;
import pda.keywordream.utils.ApiUtils.ApiResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks/{stockCode}/news")
public class NewsController {

    private final NewsService newsService;
    private final StockService stockService;

    @GetMapping("/{newsId}")
    public ResponseEntity<ApiResult<NewsResDto>> getNews(
            @PathVariable String stockCode,
            @PathVariable Long newsId
    ){
        stockService.checkStock(stockCode);
        NewsResDto newsResDto = newsService.getNews(newsId);
        return ResponseEntity.ok(ApiUtils.success(newsResDto));
    }

}
