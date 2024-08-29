package pda.keywordream.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StockDailyPriceResDto {

    private String date;
    private String price;
    private String acmlVol; //누적 거래량
    private String acmlTrPbmn; //누적 거래 대금

}
