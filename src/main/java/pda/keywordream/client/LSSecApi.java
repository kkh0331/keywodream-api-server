package pda.keywordream.client;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import pda.keywordream.client.dto.lssec.*;
import pda.keywordream.stock.entity.Stock;
import pda.keywordream.stock.repository.StockRepository;
import pda.keywordream.utils.token.LSSecToken;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class LSSecApi {

    private final LSSecToken lsSecToken;
    private final StockRepository stockRepository;

    public T8412Res fetchStockChartPrices(String stockCode, String chartDate, Integer minInterval){
        try{
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://openapi.ebestsec.co.kr:8080/stock/chart")
                    .defaultHeaders(headers -> {
                        headers.add("content-type", "application/json; charset=utf-8");
                        headers.add("authorization", "Bearer " + lsSecToken.getToken());
                        headers.add("tr_cd", "t8412");
                        headers.add("tr_cont", "N");
                        headers.add("tr_cont_key", "");
                        headers.add("mac_address", "");
                    })
                    .build();
            T8412ReqBlock t8412ReqBlock = new T8412ReqBlock(stockCode, chartDate, minInterval);
            return webClient.post()
                    .bodyValue(new T8412Req(t8412ReqBlock))
                    .retrieve()
                    .bodyToMono(T8412Res.class)
                    .block();
        } catch(Exception e){
            log.error("FetchStockChartPrices Error = {}", e.getMessage());
            throw new RuntimeException("LS 증권에서 N분 간격 주식 가져오기 실패");
        }
    }

    public void fetchStocks(){
        try{
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://openapi.ebestsec.co.kr:8080/stock/etc")
                    .defaultHeaders(headers -> {
                        headers.add("content-type", "application/json; charset=utf-8");
                        headers.add("authorization", "Bearer " + lsSecToken.getToken());
                        headers.add("tr_cd", "t8430");
                        headers.add("tr_cont", "N");
                        headers.add("tr_cont_key", "");
                        headers.add("mac_address", "");
                    })
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(config -> config.defaultCodecs().maxInMemorySize(1024*1024))
                            .build())
                    .build();
            T8430Res fetchStockRes = webClient.post()
                    .bodyValue(new T8430Req(new T8430ReqBlock("0")))
                    .retrieve()
                    .bodyToMono(T8430Res.class)
                    .timeout(Duration.ofSeconds(2))
                    .block();
            List<Stock> stocks = fetchStockRes.getT8430OutBlock().stream()
                    .map(T8430ResBlock::toStock).toList();
            stockRepository.deleteAll();
            stockRepository.saveAll(stocks);
        } catch(Exception e){
            log.error("FetchStocks Error = {}", e.getMessage());
            fetchStocks();
        }
    }

}
