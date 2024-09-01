package pda.keywordream.client.dto.shinhansec;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RankStock {

    @JsonProperty("stbd_nm")
    private String name;

    @JsonProperty("stock_code")
    private String code;
}
