package pda.keywordream.rank.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RankStock {

    @JsonProperty("stbd_nm")
    private String name;

    @JsonProperty("stock_code")
    private String code;
}
