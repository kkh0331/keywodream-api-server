package pda.keywordream.stock.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetStockResDto {

    private String code;
    private String name;
    private String price;
    private String ratio;

}
