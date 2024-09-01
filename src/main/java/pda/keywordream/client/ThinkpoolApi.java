package pda.keywordream.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pda.keywordream.client.dto.thinkpool.TopKeywordRes;
import pda.keywordream.client.dto.thinkpool.TopKeywordStock;

import java.util.List;

@Slf4j
@Component
public class ThinkpoolApi {

    private final WebClient webClient;

    public ThinkpoolApi(){
        this.webClient = WebClient.builder()
                .baseUrl("https://api.thinkpool.com/socialAnalysis")
                .build();
    }

    public TopKeywordRes fetchTopKeywords(){
        try{
            return webClient.get()
                    .uri("/keyword")
                    .retrieve()
                    .bodyToMono(TopKeywordRes.class)
                    .block();
        } catch(Exception e){
            throw new RuntimeException("thinkpool에서 키워드 리스트 가져오기 실패");
        }
    }

    public List<TopKeywordStock> fetchTopKeywordStocks(Long issn) {
        try{
            return webClient.get()
                    .uri("/keywordCodeList?&issn="+issn)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<TopKeywordStock>>() {
                    })
                    .block();
        } catch(Exception e){
            log.error("fetchRankKeywordStocks = {}", e.getMessage());
            throw new RuntimeException("thinkpool에서 issn " + issn + " 관련 주식들 가져오기 실패");
        }
    }

}
