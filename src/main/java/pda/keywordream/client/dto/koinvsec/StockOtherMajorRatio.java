package pda.keywordream.client.dto.koinvsec;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class StockOtherMajorRatio {

    @JsonProperty("stac_yymm")
    private String yearMonth;

    @JsonProperty("ev_ebitda")
    private Double evEbitda;

}
