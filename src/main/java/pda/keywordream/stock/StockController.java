package pda.keywordream.stock;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pda.keywordream.stock.dto.GetStockResDto;
import pda.keywordream.stock.dto.GetStocksReqDto;
import pda.keywordream.stock.dto.GetStocksResDto;
import pda.keywordream.stock.service.StockService;
import pda.keywordream.user.entity.User;
import pda.keywordream.user.service.UserService;
import pda.keywordream.utils.ApiUtils;
import pda.keywordream.utils.ApiUtils.ApiResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<ApiResult<GetStocksResDto>> getStocks(
            @RequestHeader("accessToken") String token,
            @RequestParam int page,
            @RequestParam int limit,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @Valid GetStocksReqDto reqDto
    ){
        User user = userService.getUser(token);
        GetStocksResDto getStocksResDto = stockService.getStocks(reqDto, user.getId());
        return ResponseEntity.ok(ApiUtils.success(getStocksResDto));
    }

    @GetMapping("/{stockCode}")
    public ResponseEntity<ApiResult<GetStockResDto>> getStock(
            @PathVariable String stockCode
    ){
        GetStockResDto getStockResDto = stockService.getStock(stockCode);
        return ResponseEntity.ok(ApiUtils.success(getStockResDto));
    }


}
