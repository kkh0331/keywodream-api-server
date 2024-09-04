package pda.keywordream.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pda.keywordream.client.dto.koinvsec.*;
import pda.keywordream.utils.token.KoInvSecToken;

import java.time.Duration;

@Slf4j
@Component
public class KoInvSecApi {

    private final WebClient webClient;

    @Autowired
    private KoInvSecToken koInvSecToken;

    public KoInvSecApi(@Value("${ko.inv.sec.app-key}") String appKey, @Value("${ko.inv.sec.app-secret}") String appSecret){
        this.webClient = WebClient.builder()
                .baseUrl("https://openapi.koreainvestment.com:9443")
                .defaultHeaders(headers -> {
                    headers.add("content-type", "application/json; charset=utf-8");
                    headers.add("appkey", appKey);
                    headers.add("appsecret", appSecret);
                })
                .build();
    }

    public StockPriceRes fetchStockPrice(String stockCode){
        try{
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/quotations/inquire-price")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_INPUT_ISCD", stockCode)
                            .build())
                    .headers(headers -> {
                        headers.add("authorization", "Bearer " + koInvSecToken.getToken());
                        headers.add("tr_id", "FHKST01010100");
                    })
                    .retrieve()
                    .bodyToMono(StockPriceRes.class)
                    .block();
        } catch(Exception e){
            log.error("fetchStockPrice = {}", e.getMessage());
            throw new RuntimeException("한국투자증권에서 해당 주식 정보 가져오기 실패");
        }
    }

    public StockDailyPriceRes fetchStockDailyPrice(String stockCode, String startDate, String endDate){
        try{
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_INPUT_ISCD", stockCode)
                            .queryParam("FID_INPUT_DATE_1", startDate)
                            .queryParam("FID_INPUT_DATE_2", endDate)
                            .queryParam("FID_PERIOD_DIV_CODE", "D")
                            .queryParam("FID_ORG_ADJ_PRC", "0")
                            .build())
                    .headers(headers -> {
                        headers.add("authorization", "Bearer " + koInvSecToken.getToken());
                        headers.add("tr_id", "FHKST03010100");
                        headers.add("custtype", "P");
                    })
                    .retrieve()
                    .bodyToMono(StockDailyPriceRes.class)
                    .block();
        } catch(Exception e){
            log.error("fetchStockDailyPrice = {}", e.getMessage());
            throw new RuntimeException("한국투자증권에서 해당 주식 daily-price 가져오기 실패");
        }
    }

    public StockIncomeStateRes fetchIncomeState(String stockCode){
        try{
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/finance/income-statement")
                            .queryParam("FID_DIV_CLS_CODE", "0")
                            .queryParam("fid_cond_mrkt_div_code", "J")
                            .queryParam("fid_input_iscd", stockCode)
                            .build())
                    .headers(headers -> {
                        headers.add("authorization", "Bearer " + koInvSecToken.getToken());
                        headers.add("tr_id", "FHKST66430200");
                        headers.add("custtype", "P");
                    })
                    .retrieve()
                    .bodyToMono(StockIncomeStateRes.class)
                    .block();
        } catch(Exception e){
            log.error("fetchIncomeState = {}", e.getMessage());
            throw new RuntimeException("한국투자증권에서 해당 주식 손익계산서 가져오기 실패");
        }
    }

    public StockFinancialRatioRes fetchFinancialRatio(String stockCode){
        try{
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/finance/financial-ratio")
                            .queryParam("FID_DIV_CLS_CODE", "0")
                            .queryParam("fid_cond_mrkt_div_code", "J")
                            .queryParam("fid_input_iscd", stockCode)
                            .build())
                    .headers(headers -> {
                        headers.add("authorization", "Bearer " + koInvSecToken.getToken());
                        headers.add("tr_id", "FHKST66430300");
                        headers.add("custtype", "P");
                    })
                    .retrieve()
                    .bodyToMono(StockFinancialRatioRes.class)
                    .block();
        } catch(Exception e){
            log.error("fetchIncomeState = {}", e.getMessage());
            throw new RuntimeException("한국투자증권에서 해당 주식 손익계산서 가져오기 실패");
        }
    }

    public StockOtherMajorRatioRes fetchOtherMajorRatio(String stockCode){
        try{
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/finance/other-major-ratios")
                            .queryParam("FID_DIV_CLS_CODE", "0")
                            .queryParam("fid_cond_mrkt_div_code", "J")
                            .queryParam("fid_input_iscd", stockCode)
                            .build())
                    .headers(headers -> {
                        headers.add("authorization", "Bearer " + koInvSecToken.getToken());
                        headers.add("tr_id", "FHKST66430500");
                        headers.add("custtype", "P");
                    })
                    .retrieve()
                    .bodyToMono(StockOtherMajorRatioRes.class)
                    .block();
        } catch(Exception e){
            log.error("fetchIncomeState = {}", e.getMessage());
            throw new RuntimeException("한국투자증권에서 해당 주식 손익계산서 가져오기 실패");
        }
    }

}
