package pda.keywordream.rank;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pda.keywordream.client.dto.google.TrendingSearchResDto;
import pda.keywordream.rank.dto.*;
import pda.keywordream.rank.service.RankService;
import pda.keywordream.rank.type.Sorting;
import pda.keywordream.user.entity.User;
import pda.keywordream.user.service.UserService;
import pda.keywordream.utils.ApiUtils;
import pda.keywordream.utils.ApiUtils.ApiResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranks")
public class RankController {

    private final RankService rankService;
    private final UserService userService;

    @GetMapping("/searches")
    public ResponseEntity<ApiResult<List<TrendingSearchResDto>>> getTrendingSearches(@Valid @ModelAttribute TrendingSearchReqDto reqDto){
        List<TrendingSearchResDto> trendingSearchResDtos = rankService.getTrendingSerches(reqDto.getLimit());
        return ResponseEntity.ok(ApiUtils.success(trendingSearchResDtos));
    }

    @GetMapping("/keywords")
    public ResponseEntity<ApiResult<List<RankKeywordResDto>>> getRankKeywords(){
        List<RankKeywordResDto> rankKeywordResDtos = rankService.getTopKeywords();
        return ResponseEntity.ok(ApiUtils.success(rankKeywordResDtos));
    }

    @GetMapping("/keywords/{issn}/stocks")
    public ResponseEntity<ApiResult<List<TopKeywordStockResDto>>> getRankKeywordStocks(
            @PathVariable Long issn
    ){
        List<TopKeywordStockResDto> topKeywordStockResDtos = rankService.getTopKeywordStocks(issn);
        return ResponseEntity.ok(ApiUtils.success(topKeywordStockResDtos));
    }

    @GetMapping("/stocks/volume")
    public ResponseEntity<ApiResult<List<RankStockResDto>>> getRankStockByVolume(
            @RequestHeader("accessToken") String token
    ){
        User user = userService.getUser(token);
        List<RankStockResDto> rankStocks = rankService.getRankStocks(user.getId(), Sorting.VOLUME);
        return ResponseEntity.ok(ApiUtils.success(rankStocks));
    }

    @GetMapping("/stocks/rising")
    public ResponseEntity<ApiResult<List<RankStockResDto>>> getRankStockByRising(
            @RequestHeader("accessToken") String token
    ){
        User user = userService.getUser(token);
        List<RankStockResDto> rankStocks = rankService.getRankStocks(user.getId(), Sorting.RISING);
        return ResponseEntity.ok(ApiUtils.success(rankStocks));
    }

    @GetMapping("/stocks/views")
    public ResponseEntity<ApiResult<List<RankStockResDto>>> getRankStockByViews(
            @RequestHeader("accessToken") String token
    ){
        User user = userService.getUser(token);
        List<RankStockResDto> rankStocks = rankService.getRankStocks(user.getId(), Sorting.VIEWS);
        return ResponseEntity.ok(ApiUtils.success(rankStocks));
    }



}
