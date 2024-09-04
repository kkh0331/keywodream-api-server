package pda.keywordream.statement.entity;

import jakarta.persistence.*;
import lombok.*;
import pda.keywordream.statement.dto.StatementResDto;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "statements")
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4)
    private String year;

    private Double revenue;

    private Double operatingIncome;

    private Double netIncome;

    private Double roe;

    private Double eps;

    private Double bps;

    private Double reserveRate;

    private Double evEbitda;

    private Double per;

    private Double pbr;

    private Double liabilityRate;

    @Column(length = 6)
    private String stockCode;

    public StatementResDto toStatementResDto(){
        return StatementResDto.builder()
                .year(year)
                .revenue(revenue)
                .operatingIncome(operatingIncome)
                .netIncome(netIncome)
                .roe(roe)
                .eps(eps)
                .bps(bps)
                .reserveRate(reserveRate)
                .evEbitda(evEbitda)
                .per(per)
                .pbr(pbr)
                .liabilityRate(liabilityRate)
                .build();
    }

}
