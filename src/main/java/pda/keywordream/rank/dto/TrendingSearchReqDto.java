package pda.keywordream.rank.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrendingSearchReqDto {

    @Min(value = 1, message = "limit은 1 이상이어야 합니다.")
    private Integer limit;

}
