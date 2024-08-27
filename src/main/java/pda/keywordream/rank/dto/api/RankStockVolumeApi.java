package pda.keywordream.rank.dto.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RankStockVolumeApi {
    private RankStockDataHeader dataHeader;
    private List<RankStock> dataBody;
}
