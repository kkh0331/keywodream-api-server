package pda.keywordream.news.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import pda.keywordream.news.dto.NewsResDto;

import java.sql.Timestamp;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "news")
public class News {

    @Id
    private Long id;

    @Column(length = 127, nullable = false)
    private String title;

    @Column(length = 32768, nullable = false)
    private String content;

    @Column(length = 15, nullable = false)
    private String press;

    @Column(length = 511)
    private String imgUrl;

    @Column(length = 511, nullable = false)
    private String originUrl;

    private Boolean isGood;

    @Column(nullable = false)
    private Timestamp createdAt;

}
