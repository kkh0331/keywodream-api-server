package pda.keywordream.rank.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pda.keywordream.rank.dto.api.RankKeywordApi;
import pda.keywordream.rank.dto.api.RankKeywordStock;

import java.util.List;

@Slf4j
@Component
public class ThinkpoolClient {

    public RankKeywordApi fetchRankKeywords(){
        try{
            WebClient webClient = WebClient.create();
            String thinkpoolUrl = "https://api.thinkpool.com/socialAnalysis/keyword";
            return webClient.get()
                    .uri(thinkpoolUrl)
                    .retrieve()
                    .bodyToMono(RankKeywordApi.class)
                    .block();
        } catch(Exception e){
            throw new RuntimeException("thinkpool에서 키워드 리스트 가져오기 실패");
        }
    }

    public List<RankKeywordStock> fetchRankKeywordStocks(Long issn) {
        try{
            WebClient webClient = WebClient.create();
            String thinkpoolUrl = "https://api.thinkpool.com/socialAnalysis/keywordCodeList?&issn="+issn;
            return webClient.get()
                    .uri(thinkpoolUrl)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<RankKeywordStock>>() {
                    })
                    .block();
        } catch(Exception e){
            log.error("fetchRankKeywordStocks = {}", e.getMessage());
            throw new RuntimeException("thinkpool에서 issn " + issn + " 관련 주식들 가져오기 실패");
        }
    }

}
