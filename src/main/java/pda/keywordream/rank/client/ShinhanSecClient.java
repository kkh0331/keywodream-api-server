package pda.keywordream.rank.client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pda.keywordream.rank.dto.api.RankStock;
import pda.keywordream.rank.dto.api.RankStockVolumeApi;

import java.util.List;

@Slf4j
@Component
public class ShinhanSecClient {

    private final WebClient webClient;

    public ShinhanSecClient(@Value("${shinhan.sec.api-key}") String apiKey){
        this.webClient = WebClient.builder()
                .baseUrl("https://gapi.shinhaninvest.com:8443/openapi/v1.0/ranking")
                .defaultHeader("apiKey", apiKey)
                .build();
    }

    @Getter
    private List<RankStock> rankStocks;

    // 신한투자증권 Open API로 거래량 순으로 상위 5개 가져온다.
    public void fetchRankStockVolume(){
        try{
            RankStockVolumeApi rankStockVolume = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/issue").queryParam("query_type", 1).build())
                    .retrieve()
                    .bodyToMono(RankStockVolumeApi.class)
                    .block();
            rankStocks = rankStockVolume.getDataBody();
        } catch(Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("신한투자증권에서 거래량 순으로 종목 가져오기 실패");
        }
    }


}
