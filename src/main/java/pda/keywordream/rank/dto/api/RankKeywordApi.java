package pda.keywordream.rank.dto.api;

import lombok.Getter;

import java.util.List;

@Getter
public class RankKeywordApi {
    private String latestDate;
    private List<RankKeyword> list;
}
