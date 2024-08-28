package pda.keywordream.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@AllArgsConstructor
@Getter
@Builder
public class GetStocksResDto {

    private PageNation pageNation;
    private List<StockResDto> stocks;

}
