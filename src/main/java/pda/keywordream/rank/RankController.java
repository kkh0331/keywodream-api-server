package pda.keywordream.rank;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pda.keywordream.rank.dto.RankKeywordResDto;
import pda.keywordream.rank.dto.RankKeywordStockResDto;
import pda.keywordream.rank.dto.RankSearchReqDto;
import pda.keywordream.rank.dto.RankSearchResDto;
import pda.keywordream.rank.dto.api.RankKeywordStock;
import pda.keywordream.rank.service.RankService;
import pda.keywordream.utils.ApiUtils;
import pda.keywordream.utils.ApiUtils.ApiResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranks")
public class RankController {

    private final RankService rankService;

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

}
