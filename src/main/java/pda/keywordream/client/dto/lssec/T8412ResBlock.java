package pda.keywordream.client.dto.lssec;

import lombok.Getter;
import lombok.ToString;
import pda.keywordream.chart.dto.StockChartPrice;

@Getter
@ToString
public class T8412ResBlock {

    private String date;
    private String time;
    private Double open;
    private Double close;
    private Double high;
    private Double low;

    public StockChartPrice toStockChartPrice(){
        return StockChartPrice.builder()
                .date(date)
                .time(time)
                .open(open)
                .close(close)
                .high(high)
                .low(low)
                .build();
    }

}
