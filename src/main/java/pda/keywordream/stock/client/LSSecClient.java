package pda.keywordream.stock.client;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import pda.keywordream.stock.entity.Stock;
import pda.keywordream.stock.repository.StockRepository;
import pda.keywordream.utils.token.LSSecToken;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class LSSecClient {

    private final LSSecToken lsSecToken;
    private final StockRepository stockRepository;

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
            FetchStockRes fetchStockRes = webClient.post()
                    .bodyValue(new FetchStockReqData(new T8430InBlock("0")))
                    .retrieve()
                    .bodyToMono(FetchStockRes.class)
                    .timeout(Duration.ofSeconds(2))
                    .block();
            List<Stock> stocks = fetchStockRes.getT8430OutBlock().stream()
                    .map(T8430OutBlock::toStock).toList();
            stockRepository.deleteAll();
            stockRepository.saveAll(stocks);
        } catch(Exception e){
            log.error("FetchStocks Error = {}", e.getMessage());
            fetchStocks();
        }
    }

    @Getter
    @AllArgsConstructor
    private static class FetchStockReqData{
        private T8430InBlock t8430InBlock;
    }

    @Getter
    @AllArgsConstructor
    private static class T8430InBlock{
        private String gubun;
    }

    @Getter
    private static class FetchStockRes{
        private String rsp_cd;
        private String rsp_msg;
        private List<T8430OutBlock> t8430OutBlock;
    }

    @Getter
    @ToString
    private static class T8430OutBlock{
        private String memedan;
        private Long recprice;
        private String shcode;
        private Long jnilclose;
        private Long uplmtprice;
        private String expcode;
        private String hname;
        private String etfgubun;
        private Long dnlmtprice;
        private String gubun;

        public Stock toStock(){
            return Stock.builder()
                    .code(shcode)
                    .name(hname)
                    .market(gubun)
                    .build();
        }
    }

}
