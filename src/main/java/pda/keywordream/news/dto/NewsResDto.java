package pda.keywordream.news.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsResDto {
    private Long id;
    private String title;
    private String press;
    private String newsDate;
    private String imgUrl;
}
