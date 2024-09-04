package pda.keywordream.heart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "hearted_stocks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "stockCode"})
})
public class HeartStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(length = 6)
    private String stockCode;

}
