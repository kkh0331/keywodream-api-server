package pda.keywordream.rank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pda.keywordream.rank.client.GoogleTrendClient;
import pda.keywordream.rank.client.ThinkpoolClient;
import pda.keywordream.rank.dto.RankKeywordResDto;
import pda.keywordream.rank.dto.RankKeywordStockResDto;
import pda.keywordream.rank.dto.RankSearchResDto;
import pda.keywordream.rank.dto.api.RankKeywordApi;
import pda.keywordream.rank.dto.api.RankKeywordStock;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RankService {

    private final GoogleTrendClient googleTrendClient;
    private final ThinkpoolClient thinkpoolClient;

    // Google Trend 실시간 검색어에 관한 정보를 가져옴
    public List<RankSearchResDto> getRankSearches(Integer limit) {
        List<RankSearchResDto> rankSearchResDtos = googleTrendClient.fetchRankSearches();
        rankSearchResDtos.sort((o1, o2) -> Integer.compare(o2.getViewCount(), o1.getViewCount()));
        if(limit == null)
            return rankSearchResDtos;
        return rankSearchResDtos.stream().limit(limit).toList();
    }

    // thinkpool라는 사이트에서 키워드 가져옴
    public List<RankKeywordResDto> getRankKeywords() {
        RankKeywordApi rankKeywordApi = thinkpoolClient.fetchRankKeywords();
        return rankKeywordApi.getList().stream().map(rankKeyword -> RankKeywordResDto.builder()
                .issn(rankKeyword.getIssn())
                .keyword(rankKeyword.getKeyword())
                .build()).toList();
    }

    // thinkpool에서 발급받은 issn으로 관련 주식들 가져옴
    public List<RankKeywordStockResDto> getRankKeywordStocks(Long issn) {
        List<RankKeywordStock> rankKeywordStocks = thinkpoolClient.fetchRankKeywordStocks(issn);
        return rankKeywordStocks.stream().map(RankKeywordStock::toRankKeywordStockResDto).toList();
    }
}
