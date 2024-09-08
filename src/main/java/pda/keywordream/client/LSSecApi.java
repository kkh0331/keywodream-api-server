package pda.keywordream.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pda.keywordream.client.dto.lssec.*;
import pda.keywordream.stock.entity.Stock;
import pda.keywordream.stock.repository.StockRepository;
import pda.keywordream.utils.token.LSSecToken;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class LSSecApi {

    private final WebClient webClient;

    @Autowired
    private LSSecToken lsSecToken;

    @Autowired
    private StockRepository stockRepository;

    public LSSecApi(){
        this.webClient = WebClient.builder()
                .baseUrl("https://openapi.ebestsec.co.kr:8080/stock")
                .defaultHeaders(headers -> {
                    headers.add("content-type", "application/json; charset=utf-8");
                    headers.add("tr_cont", "N");
                    headers.add("tr_cont_key", "");
                    headers.add("mac_address", "");
                })
                .build();
    }

    public T8412Res fetchStockChartPrices(String stockCode, String chartDate, Integer minInterval){
        try{
            T8412ReqBlock t8412ReqBlock = new T8412ReqBlock(stockCode, chartDate, minInterval);
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/chart").build())
                    .headers(headers -> {
                        headers.add("authorization", "Bearer " + lsSecToken.getToken());
                        headers.add("tr_cd", "t8412");
                    })
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
            T8430Res fetchStockRes = webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/etc").build())
                    .headers(headers -> {
                        headers.add("authorization", "Bearer " + lsSecToken.getToken());
                        headers.add("tr_cd", "t8430");
                    })
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
