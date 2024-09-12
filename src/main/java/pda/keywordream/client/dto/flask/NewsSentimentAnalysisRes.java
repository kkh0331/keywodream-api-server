package pda.keywordream.client.dto.flask;

import lombok.Getter;
import pda.keywordream.utils.ApiUtils;

@Getter
public class NewsSentimentAnalysisRes extends ApiUtils.ApiResult<NewsSentimentAnalysis> {

    public NewsSentimentAnalysisRes(Boolean success, NewsSentimentAnalysis response, ApiUtils.ApiError error) {
        super(success, response, error);
    }

}
