package pda.keywordream.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pda.keywordream.client.dto.google.TrendingSearch;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GoogleApi {

    public List<TrendingSearch> fetchTrendingSearches(){
        List<TrendingSearch> trendingSearches = new ArrayList<>();
        try{
            String googleTrendUrl = "https://trends.google.com/trends/trendingsearches/daily/rss?geo=KR";

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(googleTrendUrl);

            NodeList items = doc.getElementsByTagName("item");
            for(int i=0; i < items.getLength(); i++){
                Element item = (Element) items.item(i);
                TrendingSearch trendingSearch = extractTrendingSearchResDto(item);
                if(trendingSearch != null){
                    trendingSearches.add(trendingSearch);
                }
            }
        } catch(Exception e){
            log.info("fetchTrendingSearches = {}", e.getMessage());
            throw new RuntimeException("Google Trend 실시간 검색어 가져오기 실패");
        }
        return trendingSearches;
    }

    private TrendingSearch extractTrendingSearchResDto(Element item){
        try{
            String title = item.getElementsByTagName("title").item(0).getTextContent();
            // viewCount -> 10,000+ -> 숫자만 추출하기 위해서 replaceAll 적용
            String viewCount = item.getElementsByTagName("ht:approx_traffic").item(0).getTextContent().replaceAll("[^\\d]", "");
            String newsUrl = item.getElementsByTagName("ht:news_item_url").item(0).getTextContent();
            String imgUrl = item.getElementsByTagName("ht:picture").item(0).getTextContent();
            return TrendingSearch.builder()
                    .title(title)
                    .viewCount(Integer.parseInt(viewCount))
                    .newsUrl(newsUrl)
                    .imgUrl(imgUrl)
                    .build();
        } catch(Exception e){
            log.info("extractTrendingSearchResDto = {}", e.getMessage());
            return null;
        }
    }

}
