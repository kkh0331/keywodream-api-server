package pda.keywordream.client.dto.google;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrendingSearch {

    private String title;
    private Integer viewCount;
    private String newsUrl;
    private String imgUrl;

}
