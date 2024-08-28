package pda.keywordream.stock.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "stocks")
public class Stock {

    @Id
    @Column(length = 6)
    private String code;

    @Column(length = 31)
    private String name;

    @Column(length = 1)
    private String market;

}
