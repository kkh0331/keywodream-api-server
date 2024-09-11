package pda.keywordream.keyword;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pda.keywordream.news.dto.NewsKeywordResDto;
import pda.keywordream.news.service.NewsService;
import pda.keywordream.stock.service.StockService;
import pda.keywordream.utils.ApiUtils;
import pda.keywordream.utils.ApiUtils.ApiResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks/{stockCode}/keywords")
public class KeywordController {

    private final StockService stockService;
    private final NewsService newsService;

    @GetMapping("")
    public ResponseEntity<ApiResult<List<NewsKeywordResDto>>> getKeywords(
            @PathVariable String stockCode,
            @RequestParam Integer count
    ){
        stockService.checkStock(stockCode);
        List<NewsKeywordResDto> newsKeywordResDtos = newsService.getNewsKeywords(stockCode, count);
        return ResponseEntity.ok(ApiUtils.success(newsKeywordResDtos));
    }

}
