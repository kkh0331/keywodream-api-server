package pda.keywordream.rank.dto.api;
import lombok.Getter;
import pda.keywordream.rank.dto.RankKeywordStockResDto;

@Getter
public class RankKeywordStock {
    private String stockCode;
    private String stockName;
    private Double ratio;
    private String company_summary;

    public RankKeywordStockResDto toRankKeywordStockResDto(){
        return RankKeywordStockResDto.builder()
                .stockCode(stockCode)
                .stockName(stockName)
                .ratio(ratio)
                .companySummary(company_summary)
                .build();
    }
}
