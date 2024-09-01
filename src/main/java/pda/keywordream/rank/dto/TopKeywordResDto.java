package pda.keywordream.rank.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopKeywordResDto {

    private Long issn;
    private String keyword;

}
