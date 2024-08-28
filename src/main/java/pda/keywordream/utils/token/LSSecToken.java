package pda.keywordream.utils.token;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class LSSecToken {

    @Getter
    private String token;
    private final String appKey;
    private final String appSecretKey;

    public LSSecToken(@Value("${ls.sec.app-key}") String appKey, @Value("${ls.sec.app-secret-key}") String appSecretKey){
        this.appKey = appKey;
        this.appSecretKey = appSecretKey;
    }

    public void generateToken(){
        try{
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://openapi.ebestsec.co.kr:8080/oauth2/token")
                    .defaultHeader("content-type", "application/x-www-form-urlencoded")
                    .build();
            LSSecTokenApi lsSecTokenApi = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("grant_type", "client_credentials")
                            .queryParam("appkey", appKey)
                            .queryParam("appsecretkey", appSecretKey)
                            .queryParam("scope", "oob")
                            .build())
                    .retrieve()
                    .bodyToMono(LSSecTokenApi.class)
                    .block();
            token = lsSecTokenApi.getAccess_token();
        } catch(Exception e){
            log.error("generateLSsecToken Error = {}", e.getMessage());
            throw new RuntimeException("LS 증권에서 토큰 발급 실패");
        }
    }

    @Getter
    public static class LSSecTokenApi{
        private String access_token;
        private String scope;
        private String token_type;
        private Long expires_in;
    }

}