package pda.keywordream.client.dto.lssec;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class T8412ReqBlock {

    private String shcode;
    private Integer ncnt;
    private Integer qrycnt;
    private String nday;
    private String sdate;
    private String stime;
    private String edate;
    private String etime;
    @JsonProperty("cts_date")
    private String ctsDate;
    @JsonProperty("cts_time")
    private String ctsTime;
    @JsonProperty("comp_yn")
    private String compYn;

    public T8412ReqBlock(String stockCode, String chartDate, Integer minInterval){
        shcode = stockCode;
        ncnt = minInterval;
        qrycnt = 2000;
        nday = "1";
        sdate = chartDate;
        stime = "090000";
        edate = chartDate;
        etime = "153000";
        ctsDate = "";
        ctsTime = "";
        compYn = "N";
    }

}
