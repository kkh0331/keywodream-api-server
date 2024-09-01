package pda.keywordream.client.dto.thinkpool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pda.keywordream.rank.dto.TopKeywordStockResDto;

@Getter
public class TopKeywordStock {

    private String stockCode;
    private String stockName;
    private Double ratio;
    @JsonProperty("company_summary")
    private String companySummary;

    public TopKeywordStockResDto toTopKeywordStockResDto(){
        return TopKeywordStockResDto.builder()
                .stockCode(stockCode)
                .stockName(stockName)
                .ratio(ratio)
                .companySummary(companySummary)
                .build();
    }


}
