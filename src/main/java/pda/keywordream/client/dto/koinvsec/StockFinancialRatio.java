package pda.keywordream.client.dto.koinvsec;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class StockFinancialRatio {
    @JsonProperty("stac_yymm")
    private String yearMonth;

    @JsonProperty("roe_val")
    private Double roe;

    private Double eps;

    private Double bps;

    @JsonProperty("rsrv_rate")
    private double reserveRate;

    @JsonProperty("lblt_rate")
    private double liabilityRate;
}
