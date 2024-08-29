package pda.keywordream.rank;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pda.keywordream.rank.dto.*;
import pda.keywordream.rank.dto.api.RankStock;
import pda.keywordream.rank.service.RankService;
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
    public ResponseEntity<ApiResult<List<RankSearchResDto>>> getRankSearches(@Valid @ModelAttribute RankSearchReqDto reqDto){
        List<RankSearchResDto> rankSearchResDtos = rankService.getRankSearches(reqDto.getLimit());
        return ResponseEntity.ok(ApiUtils.success(rankSearchResDtos));
    }

    @GetMapping("/keywords")
    public ResponseEntity<ApiResult<List<RankKeywordResDto>>> getRankKeywords(){
        List<RankKeywordResDto> rankKeywordResDtos = rankService.getRankKeywords();
        return ResponseEntity.ok(ApiUtils.success(rankKeywordResDtos));
    }

    @GetMapping("/keywords/{issn}/stocks")
    public ResponseEntity<ApiResult<List<RankKeywordStockResDto>>> getRankKeywordStocks(
            @PathVariable Long issn
    ){
        List<RankKeywordStockResDto> rankKeywordStockResDtos = rankService.getRankKeywordStocks(issn);
        return ResponseEntity.ok(ApiUtils.success(rankKeywordStockResDtos));
    }

    @GetMapping("/stocks/volume")
    public ResponseEntity<ApiResult<List<RankStockResDto>>> getRankStockVolume(
            @RequestHeader("accessToken") String token
    ){
        User user = userService.getUser(token);
        List<RankStockResDto> rankStocks = rankService.getRankStockVolume(user.getId());
        return ResponseEntity.ok(ApiUtils.success(rankStocks));
    }



}
