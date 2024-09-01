package pda.keywordream.rank.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TopKeywordStockResDto {
    private String stockCode;
    private String stockName;
    private Double ratio;
    private String companySummary;
}
