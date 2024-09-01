package pda.keywordream.client.dto.google;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrendingSearchResDto {

    private String title;
    private Integer viewCount;
    private String newsUrl;
    private String imgUrl;

}
