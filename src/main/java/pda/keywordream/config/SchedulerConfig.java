package pda.keywordream.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pda.keywordream.rank.client.ShinhanSecClient;
import pda.keywordream.stock.client.LSSecClient;
import pda.keywordream.utils.token.LSSecToken;

@Slf4j
@Component
@EnableScheduling
@AllArgsConstructor
public class SchedulerConfig {

    private ShinhanSecClient shinhanSecClient;
    private LSSecClient lsSecClient;
    private LSSecToken lsSecToken;

//    @Scheduled(fixedDelay = 1000 * 60 * 5)
//    public void runShinhanSecClient(){
//        shinhanSecClient.fetchRankStockVolume();
//        log.info("신한투자증권 Open API - 실시간 인기 종목 업데이트");
//    }

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void runLSSecToken(){
        lsSecToken.generateToken();
        log.info("LS투자증권 Open API - 토큰 발급");
    }

    // 주식 종목은 매일 상장되고 폐지된다 -> 오전 6시마다 다시 받아온다.
    @Scheduled(cron = "0 0 6 * * ?")
    public void runLSSecClient(){
        lsSecClient.fetchStocks();
        log.info("LS투자증권 Open API - 주식 종목 가져오기");
    }

}
