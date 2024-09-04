package pda.keywordream.statement.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatementResDto {

    private String year;

    private Double revenue;

    private Double operatingIncome;

    private Double netIncome;

    private Double roe;

    private Double eps;

    private Double bps;

    private Double reserveRate;

    private Double evEbitda;

    private Double per;

    private Double pbr;

    private Double liabilityRate;

}
