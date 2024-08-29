package pda.keywordream.rank.dto.api;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RankStockByViewsApi {
    private RankStockDataHeader dataHeader;
    private RankStockByViewsDataBody dataBody;
}
