package pda.keywordream.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pda.keywordream.rank.client.ShinhanSecClient;

@Slf4j
@Component
@EnableScheduling
@AllArgsConstructor
public class SchedulerConfig {

    private ShinhanSecClient shinhanSecClient;

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void runShinhanSecClient(){
        shinhanSecClient.fetchRankStockVolume();
        log.info("신한투자증권 Open API - 실시간 인기 종목 업데이트");
    }

}
