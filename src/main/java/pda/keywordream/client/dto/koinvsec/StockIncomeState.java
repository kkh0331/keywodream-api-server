package pda.keywordream.client.dto.koinvsec;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class StockIncomeState {
    @JsonProperty("stac_yymm")
    private String yearMonth;

    @JsonProperty("sale_account")
    private Double revenue;

    @JsonProperty("bsop_prti")
    private Double operatingIncome;

    @JsonProperty("thtr_ntin")
    private Double netIncome;

}
