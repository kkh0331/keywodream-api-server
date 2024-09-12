package pda.keywordream.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pda.keywordream.client.dto.flask.NewsSentimentAnalysisRes;
import pda.keywordream.utils.ApiUtils.ApiResult;

@Slf4j
@Component
public class FlaskApi {

    private final WebClient webClient;

    public FlaskApi(){
        this.webClient = WebClient.builder()
                .baseUrl("http://127.0.0.1:5000/api")
                .build();
    }

    public NewsSentimentAnalysisRes analyzeNewsSentiment(Long newsId){
        try{
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/news/"+newsId+"/sentiment-analysis")
                            .build())
                    .retrieve()
                    .bodyToMono(NewsSentimentAnalysisRes.class)
                    .block();
        } catch(Exception e){
            log.error("analyzeNewsSentiment Error = {}", e.getMessage());
            throw new RuntimeException("Flask 크롤링 서버에서 뉴스 감정분석 실패");
        }
    }



}
