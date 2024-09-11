package pda.keywordream.news.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsResDto {

    private String title;
    private String press;
    private String newsDate;
    private Boolean isGood;
    private String content;

}
