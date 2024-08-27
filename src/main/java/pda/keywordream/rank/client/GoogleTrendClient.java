package pda.keywordream.rank.client;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pda.keywordream.rank.dto.RankSearchResDto;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleTrendClient {

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

}
