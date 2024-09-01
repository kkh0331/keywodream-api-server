package pda.keywordream.client.dto.koinvsec;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pda.keywordream.stock.dto.StockDailyPriceResDto;

@Getter
public class StockDailyPrice {

    @JsonProperty("stck_bsop_date")
    private String date;

    @JsonProperty("stck_clpr")
    private String price;

    @JsonProperty("acml_vol")
    private String acmlVol; //누적 거래량

    @JsonProperty("acml_tr_pbmn")
    private String acmlTrPbmn; //누적 거래 대금

    public StockDailyPriceResDto toStockDailyPriceResDto(){
        return StockDailyPriceResDto.builder()
                .date(date)
                .price(price)
                .acmlVol(acmlVol)
                .acmlTrPbmn(acmlTrPbmn)
                .build();
    }

}
