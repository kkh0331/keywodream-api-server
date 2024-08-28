package pda.keywordream.utils.token;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class KoInvSecToken {

    @Getter
    private String token;
    private final String appKey;
    private final String appSecret;

    public KoInvSecToken(@Value("${ko.inv.sec.app-key}") String appKey, @Value("${ko.inv.sec.app-secret}") String appSecret){
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    public void generateToken(){
        try{
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://openapi.koreainvestment.com:9443/oauth2/tokenP")
                    .defaultHeader("Content-Type", "application/json")
                    .build();

            Map<String, String> reqData = new HashMap<>();
            reqData.put("grant_type", "client_credentials");
            reqData.put("appkey", appKey);
            reqData.put("appsecret", appSecret);

            KOInvSecTokenApi koInvSecTokenApi = webClient.post()
                    .bodyValue(reqData)
                    .retrieve()
                    .bodyToMono(KOInvSecTokenApi.class)
                    .timeout(Duration.ofSeconds(2))
                    .block();
            token = koInvSecTokenApi.getAccess_token();
        } catch(Exception e){
            log.error("generateKoInvSecToken error = {}", e.getMessage());
            generateToken();
        }
    }

    @Getter
    private static class KOInvSecTokenApi{
        private String access_token;
        private String access_token_token_expired;
        private String token_type;
        private Long expires_in;
    }

}
