package pda.keywordream.rank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pda.keywordream.client.GoogleApi;
import pda.keywordream.client.ShinhanSecApi;
import pda.keywordream.client.ThinkpoolApi;
import pda.keywordream.client.dto.google.TrendingSearch;
import pda.keywordream.client.dto.thinkpool.TopKeywordRes;
import pda.keywordream.client.dto.thinkpool.TopKeywordStock;
import pda.keywordream.heart.entity.HeartStock;
import pda.keywordream.heart.repository.HeartStockRepository;
import pda.keywordream.rank.dto.TopKeywordResDto;
import pda.keywordream.rank.dto.TopKeywordStockResDto;
import pda.keywordream.rank.dto.RankStockResDto;
import pda.keywordream.client.dto.shinhansec.RankStock;
import pda.keywordream.rank.type.Sorting;
import pda.keywordream.stock.client.KoInvSecClient;
import pda.keywordream.stock.dto.api.StockPrice;
import pda.keywordream.stock.dto.api.StockPriceApi;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RankService {

    private final GoogleApi googleApi;
    private final ThinkpoolApi thinkpoolApi;
    private final ShinhanSecApi shinhanSecApi;

    private final HeartStockRepository heartStockRepository;
    private final KoInvSecClient koInvSecClient;

    // Google Trend 실시간 검색어에 관한 정보를 가져옴
    public List<TrendingSearch> getTrendingSerches(Integer limit) {
        List<TrendingSearch> trendingSearches = googleApi.fetchTrendingSearches();
        trendingSearches.sort((o1, o2) -> Integer.compare(o2.getViewCount(), o1.getViewCount()));
        if(limit == null)
            return trendingSearches;
        return trendingSearches.stream().limit(limit).toList();
    }

    // thinkpool라는 사이트에서 키워드 가져옴
    public List<TopKeywordResDto> getTopKeywords() {
        TopKeywordRes topKeywordRes = thinkpoolApi.fetchTopKeywords();
        return topKeywordRes.getList().stream().map(rankKeyword -> TopKeywordResDto.builder()
                .issn(rankKeyword.getIssn())
                .keyword(rankKeyword.getKeyword())
                .build())
                .toList();
    }

    // thinkpool에서 발급받은 issn으로 관련 주식들 가져옴
    public List<TopKeywordStockResDto> getTopKeywordStocks(Long issn) {
        List<TopKeywordStock> rankKeywordStocks = thinkpoolApi.fetchTopKeywordStocks(issn);
        return rankKeywordStocks.stream().map(TopKeywordStock::toTopKeywordStockResDto).toList();
    }

    public List<RankStockResDto> getRankStocks(Long userId, Sorting sorting) {
        List<String> heartedStockCodes = heartStockRepository.findAllByUserId(userId).stream()
                .map(HeartStock::getStockCode).toList();
        List<RankStock> rankStocks = getRankStocksBySorting(sorting);
        return rankStocks.stream().map(rankStock -> {
            StockPriceApi stockPriceApi = koInvSecClient.fetchStockPrice(rankStock.getCode());
            StockPrice stockPrice = stockPriceApi.getOutput();
            return RankStockResDto.builder()
                    .code(rankStock.getCode())
                    .name(rankStock.getName())
                    .price(stockPrice.getPrice())
                    .ratio(stockPrice.getRatio())
                    .isHearted(heartedStockCodes.contains(rankStock.getCode()))
                    .build();
        }).toList();
    }

    public List<RankStock> getRankStocksBySorting(Sorting sorting){
        return switch (sorting){
            case VOLUME -> shinhanSecApi.getRankStocksByVolume();
            case VIEWS -> shinhanSecApi.getRankStocksByViews();
            case RISING -> shinhanSecApi.getRankStocksByRising();
        };
    }
}
