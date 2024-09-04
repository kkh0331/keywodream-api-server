package pda.keywordream.statement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pda.keywordream.statement.dto.StatementResDto;
import pda.keywordream.statement.service.StatementService;
import pda.keywordream.stock.service.StockService;
import pda.keywordream.utils.ApiUtils;
import pda.keywordream.utils.ApiUtils.ApiResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks/{stockCode}/statements")
public class StatementController {

    private final StatementService statementService;
    private final StockService stockService;

    @GetMapping("")
    public ResponseEntity<ApiResult<StatementResDto>> getStatement(
            @PathVariable String stockCode
    ){
        stockService.checkStock(stockCode);
        StatementResDto statementResDto = statementService.getStatement(stockCode);
        return ResponseEntity.ok(ApiUtils.success(statementResDto));
    }

}
