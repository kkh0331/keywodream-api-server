package pda.keywordream.client.dto.lssec;

import lombok.Getter;
import lombok.ToString;
import pda.keywordream.stock.entity.Stock;

@Getter
@ToString
public class T8430ResBlock {
    private String shcode;
    private String hname;
    private String gubun;

    public Stock toStock(){
        return Stock.builder()
                .code(shcode)
                .name(hname)
                .market(gubun)
                .build();
    }
}
