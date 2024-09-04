package pda.keywordream.statement.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pda.keywordream.client.KoInvSecApi;
import pda.keywordream.client.dto.koinvsec.*;
import pda.keywordream.statement.dto.PerPbrDto;
import pda.keywordream.statement.dto.StatementResDto;
import pda.keywordream.statement.entity.Statement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Service
public class StatementService {

    private final KoInvSecApi koInvSecApi;

    public StatementResDto getStatement(String stockCode) {
        String yymm = getYearMonth(1);
        String year = yymm.substring(0,4);
        StockIncomeState stockIncomeStateByYYMM = fetchStockIncomeState(stockCode, yymm);
        StockFinancialRatio stockFinancialRatioByYYMM = fetchStockFinancialRatio(stockCode, yymm);
        StockOtherMajorRatio stockOtherMajorRatioByYYMM = fetchStockOtherMajorRatio(stockCode, yymm);
        Double eps = stockFinancialRatioByYYMM.getEps();
        Double bps = stockFinancialRatioByYYMM.getBps();
        PerPbrDto perPbrDto = fetchPerPbr(year, stockCode, eps, bps);
        Statement statement = Statement.builder()
                .year(year)
                .revenue(stockIncomeStateByYYMM.getRevenue())
                .operatingIncome(stockIncomeStateByYYMM.getOperatingIncome())
                .netIncome(stockIncomeStateByYYMM.getNetIncome())
                .roe(stockFinancialRatioByYYMM.getRoe())
                .eps(eps)
                .bps(bps)
                .reserveRate(stockFinancialRatioByYYMM.getReserveRate())
                .liabilityRate(stockFinancialRatioByYYMM.getLiabilityRate())
                .evEbitda(stockOtherMajorRatioByYYMM.getEvEbitda())
                .per(perPbrDto.getPer())
                .pbr(perPbrDto.getPbr())
                .stockCode(stockCode)
                .build();

        return statement.toStatementResDto();
    }

    private StockIncomeState fetchStockIncomeState(String stockCode, String yymm){
        return koInvSecApi.fetchIncomeState(stockCode)
                .getOutput()
                .stream()
                .filter(stockIncomeState -> Objects.equals(stockIncomeState.getYearMonth(), yymm))
                .findFirst()
                .orElse(new StockIncomeState());
    }

    private StockFinancialRatio fetchStockFinancialRatio(String stockCode, String yymm){
        return koInvSecApi.fetchFinancialRatio(stockCode)
                .getOutput()
                .stream()
                .filter(stockFinancialRatio -> Objects.equals(stockFinancialRatio.getYearMonth(), yymm))
                .findFirst()
                .orElse(new StockFinancialRatio());
    }

    private StockOtherMajorRatio fetchStockOtherMajorRatio(String stockCode, String yymm){
        return koInvSecApi.fetchOtherMajorRatio(stockCode)
                .getOutput()
                .stream()
                .filter(stockOtherMajorRatio -> Objects.equals(stockOtherMajorRatio.getYearMonth(), yymm))
                .findFirst()
                .orElse(new StockOtherMajorRatio());
    }

    private PerPbrDto fetchPerPbr(String year, String stockCode, Double eps, Double bps){
        StockDailyPriceRes stockDailyPriceRes = koInvSecApi.fetchStockDailyPrice(stockCode, year+"1201", year+"1231");
        StockDailyPrice stockDailyPrice = stockDailyPriceRes.getOutput2().get(0); // 해당 년도 마지막 날 데이터
        Double stockPrice = Double.parseDouble(stockDailyPrice.getPrice());
        return PerPbrDto.builder()
                .per(stockPrice/eps)
                .pbr(stockPrice/bps)
                .build();
    }

    private String getYearMonth(int beforeYear){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -beforeYear);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy12");
        return sdf.format(cal.getTime());
    }

}
