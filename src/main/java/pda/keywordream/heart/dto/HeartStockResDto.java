package pda.keywordream.heart.dto;

import lombok.Builder;
import lombok.Getter;
import pda.keywordream.stock.entity.Stock;

@Getter
@Builder
public class HeartStockResDto {

    private Long id;
    private Stock stock;

}
