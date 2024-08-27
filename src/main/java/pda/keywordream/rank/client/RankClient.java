package pda.keywordream.rank.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pda.keywordream.rank.dto.RankSearchResDto;
import pda.keywordream.rank.dto.api.RankKeywordApi;
import pda.keywordream.rank.dto.api.RankKeywordStock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RankClient {

    public List<RankSearchResDto> fetchRankSearches(){
        List<RankSearchResDto> rankSearchResDtos = new ArrayList<>();
        try{
            String googleTrendUrl = "https://trends.google.com/trends/trendingsearches/daily/rss?geo=KR";

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(googleTrendUrl);

            NodeList items = doc.getElementsByTagName("item");
            for(int i=0; i < items.getLength(); i++){
                Element item = (Element) items.item(i);
                RankSearchResDto rankSearchResDto = extractRankSearchResDto(item);
                rankSearchResDtos.add(rankSearchResDto);
            }
        } catch(Exception e){
            throw new RuntimeException("Google Trend 실시간 검색어 가져오기 실패");
        }
        return rankSearchResDtos;
    }

    private RankSearchResDto extractRankSearchResDto(Element item){
        String title = item.getElementsByTagName("title").item(0).getTextContent();
        // viewCount -> 10,000+ -> 숫자만 추출하기 위해서 replaceAll 적용
        String viewCount = item.getElementsByTagName("ht:approx_traffic").item(0).getTextContent().replaceAll("[^\\d]", "");
        String newsUrl = item.getElementsByTagName("ht:news_item_url").item(0).getTextContent();
        String imgUrl = item.getElementsByTagName("ht:picture").item(0).getTextContent();
        return RankSearchResDto.builder()
                .title(title)
                .viewCount(Integer.parseInt(viewCount))
                .newsUrl(newsUrl)
                .imgUrl(imgUrl)
                .build();
    }

    public RankKeywordApi fetchRankKeywords(){
        try{
            WebClient webClient = WebClient.create();
            String thinkpoolUrl = "https://api.thinkpool.com/socialAnalysis/keyword";
            return webClient.get()
                    .uri(thinkpoolUrl)
                    .retrieve()
                    .bodyToMono(RankKeywordApi.class)
                    .block();
        } catch(Exception e){
            throw new RuntimeException("thinkpool에서 키워드 리스트 가져오기 실패");
        }
    }

    public List<RankKeywordStock> fetchRankKeywordStocks(Long issn) {
        try{
            WebClient webClient = WebClient.create();
            String thinkpoolUrl = "https://api.thinkpool.com/socialAnalysis/keywordCodeList?&issn="+issn;
            return webClient.get()
                    .uri(thinkpoolUrl)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<RankKeywordStock>>() {
                    })
                    .block();
        } catch(Exception e){
            log.error("fetchRankKeywordStocks = {}", e.getMessage());
            throw new RuntimeException("thinkpool에서 issn " + issn + " 관련 주식들 가져오기 실패");
        }
    }
}
