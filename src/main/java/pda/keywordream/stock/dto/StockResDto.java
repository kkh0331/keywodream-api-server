package pda.keywordream.stock.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockResDto {

    private String code;
    private String name;
    private Boolean isHearted;

}
