package pda.keywordream.client.dto.flask;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NewsSentimentAnalysis {

    @JsonProperty("is_good")
    private Boolean isGood;

    @JsonProperty("news_id")
    private String newsId;

}
