package pda.keywordream.stock.dto.api;

import lombok.Getter;

@Getter
public class StockPriceApi {

    private String rt_cd;
    private String msg_cd;
    private String msg1;
    private StockPrice output;

}
