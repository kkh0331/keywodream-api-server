package pda.keywordream.chart.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pda.keywordream.chart.dto.StockChartPrice;
import pda.keywordream.client.LSSecApi;
import pda.keywordream.client.dto.lssec.T8412Res;
import pda.keywordream.client.dto.lssec.T8412ResBlock;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ChartService {

    private final LSSecApi lsSecApi;

    public List<StockChartPrice> getStockChartPrices(String stockCode, Integer minInterval) {
        String chartDate = getChartDate();
        T8412Res t8412Res = lsSecApi.fetchStockChartPrices(stockCode, chartDate, minInterval);
        return t8412Res.getT8412OutBlock1().stream()
                .map(T8412ResBlock::toStockChartPrice)
                .toList();
    }

    private String getChartDate(){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        if(currentTime.getHour() < 9){
            return currentTime.minusDays(1).format(formatter);
        }
        return currentTime.format(formatter);
    }
}
