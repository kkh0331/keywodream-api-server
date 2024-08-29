package pda.keywordream.rank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankStockResDto {

    private String name;
    private String code;
    private String price;
    private String ratio;
    private Boolean isHearted;

}
