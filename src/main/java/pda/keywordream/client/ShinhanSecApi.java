package pda.keywordream.client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pda.keywordream.client.dto.shinhansec.RankStock;
import pda.keywordream.client.dto.shinhansec.RankStockRes;
import pda.keywordream.client.dto.shinhansec.RankStockByViewsRes;

import java.util.List;

@Slf4j
@Component
public class ShinhanSecApi {

    private final WebClient webClient;

    public ShinhanSecApi(@Value("${shinhan.sec.api-key}") String apiKey){
        this.webClient = WebClient.builder()
                .baseUrl("https://gapi.shinhaninvest.com:8443/openapi/v1.0/ranking")
                .defaultHeader("apiKey", apiKey)
                .build();
    }

    @Getter
    private List<RankStock> rankStocksByVolume;

    @Getter
    private List<RankStock> rankStocksByRising;

    @Getter
    private List<RankStock> rankStocksByViews;

    // 신한투자증권 Open API로 거래량 순으로 상위 5개 가져온다.
    public void fetchRankStockVolume(){
        try{
            RankStockRes rankStockVolume = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/issue").queryParam("query_type", 1).build())
                    .retrieve()
                    .bodyToMono(RankStockRes.class)
                    .block();
            rankStocksByVolume = rankStockVolume.getDataBody();
        } catch(Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("신한투자증권에서 거래량 순으로 종목 가져오기 실패");
        }
    }

    // 신한투자증권 Open API로 주가상승률 순으로 상위 5개 가져온다.
    public void fetchRankStockRising(){
        try{
            RankStockRes rankStockRising = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/issue").queryParam("query_type", 2).build())
                    .retrieve()
                    .bodyToMono(RankStockRes.class)
                    .block();
            rankStocksByRising = rankStockRising.getDataBody();
        } catch(Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("신한투자증권에서 주가상승률 순으로 종목 가져오기 실패");
        }
    }

    // 신한투자증권 Open API로 조회수 순으로 상위 5개 가져온다.
    public void fetchRankStockViews(){
        try{
            RankStockByViewsRes rankStockRising = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/rising").build())
                    .retrieve()
                    .bodyToMono(RankStockByViewsRes.class)
                    .block();
            rankStocksByViews = rankStockRising.getDataBody().getList();
        } catch(Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("신한투자증권에서 조회수 순으로 종목 가져오기 실패");
        }
    }



}
