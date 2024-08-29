package pda.keywordream.stock.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class StockPrice {
    @JsonProperty("stck_prpr")
    private String price;
    @JsonProperty("prdy_ctrt")
    private String ratio;
}
