package pda.keywordream.rank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pda.keywordream.rank.client.RankClient;
import pda.keywordream.rank.dto.RankSearchResDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RankService {

    private final RankClient rankClient;

    // Google Trend 실시간 검색어에 관한 정보를 가져옴
    public List<RankSearchResDto> getRankSearches(Integer limit) {
        List<RankSearchResDto> rankSearchResDtos = rankClient.fetchRankSearches();
        rankSearchResDtos.sort((o1, o2) -> Integer.compare(o2.getViewCount(), o1.getViewCount()));
        if(limit == null)
            return rankSearchResDtos;
        return rankSearchResDtos.stream().limit(limit).toList();
    }
}
