package pda.keywordream.chart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockChartPrice {

    private String date;
    private String time;
    private Double open;
    private Double close;
    private Double high;
    private Double low;

}
