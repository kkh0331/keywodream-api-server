package pda.keywordream.chart;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pda.keywordream.chart.dto.StockChartPrice;
import pda.keywordream.chart.service.ChartService;
import pda.keywordream.stock.service.StockService;
import pda.keywordream.utils.ApiUtils;
import pda.keywordream.utils.ApiUtils.ApiResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks/{stockCode}/charts")
public class ChartController {

    private final StockService stockService;
    private final ChartService chartService;

    @GetMapping("/prices")
    public ResponseEntity<ApiResult<List<StockChartPrice>>> getStockChartPrices(
            @PathVariable String stockCode,
            @RequestParam @Min(value = 1, message = "minInterval은 1보다 커야 합니다.") Integer minInterval
    ){
        stockService.checkStock(stockCode);
        List<StockChartPrice> stockChartPrices = chartService.getStockChartPrices(stockCode, minInterval);
        return ResponseEntity.ok(ApiUtils.success(stockChartPrices));
    }

}
