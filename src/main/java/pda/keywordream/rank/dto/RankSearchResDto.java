package pda.keywordream.rank.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankSearchResDto {

    private String title;
    private Integer viewCount;
    private String newsUrl;
    private String imgUrl;

}
