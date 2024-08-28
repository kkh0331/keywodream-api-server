package pda.keywordream.stock.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetStocksReqDto {

    @NotNull
    @Min(value = 1, message = "page는 1 이상이어야 합니다.")
    private int page;

    @NotNull
    @Min(value = 1, message = "limit는 1 이상이어야 합니다.")
    private int limit;

    private String code;

    private String name;

}
