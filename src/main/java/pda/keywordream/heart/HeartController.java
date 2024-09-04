package pda.keywordream.heart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pda.keywordream.heart.dto.HeartStockResDto;
import pda.keywordream.heart.dto.RegisterHeartStockReqDto;
import pda.keywordream.heart.entity.HeartStock;
import pda.keywordream.heart.service.HeartStockService;
import pda.keywordream.stock.service.StockService;
import pda.keywordream.user.entity.User;
import pda.keywordream.user.service.UserService;
import pda.keywordream.utils.ApiUtils;
import pda.keywordream.utils.ApiUtils.ApiResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hearts")
public class HeartController {

    private final HeartStockService heartStockService;
    private final StockService stockService;
    private final UserService userService;

    @PostMapping("/stocks")
    public ResponseEntity<ApiResult<String>> registerHeartStock(
            @RequestHeader("accessToken") String token,
            @RequestBody RegisterHeartStockReqDto reqDto
    ){
        User user = userService.getUser(token);
        stockService.checkStock(reqDto.getStockCode());
        heartStockService.registerHeartStock(user.getId(), reqDto.getStockCode());
        return ResponseEntity.created(null).body(ApiUtils.success("찜 기능에 추가 완료"));
    }

    @GetMapping("/stocks")
    public ResponseEntity<ApiResult<List<HeartStockResDto>>> getHeartStocks(
            @RequestHeader("accessToken") String token
    ){
        User user = userService.getUser(token);
        List<HeartStockResDto> heartStockResDtos = heartStockService.getHearStocks(user.getId());
        return ResponseEntity.ok(ApiUtils.success(heartStockResDtos));
    }

    @DeleteMapping("/stocks/{stockCode}")
    public ResponseEntity<ApiResult<String>> deleteHeartStock(
            @RequestHeader("accessToken") String token,
            @PathVariable String stockCode
    ){
        User user = userService.getUser(token);
        heartStockService.deleteHeartStock(user.getId(), stockCode);
        heartStockService.checkHeartStock(user.getId(), stockCode);
        return ResponseEntity.ok(ApiUtils.success("찜 기능에서 삭제 완료"));
    }

}
