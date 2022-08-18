package libro.libro.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    private String author;  //책의 저자
    private String writer;  //상품 등록자
    private String saveFileName;

    @Column(columnDefinition = "integer default 0")
    private int price;  //가격

    private int remaining;  //재고수량

    private String category;

    private String year;  //출판년도

    @Column(columnDefinition = "integer default 0")
    private int good;  //좋아요

    @Builder
    public Item(
            Long id,
            String title,
            String author,
            String writer,
            String saveFileName,
            int price,
            int remaining,
            String category,
            String year,
            int good
    ) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.writer = writer;
        this.saveFileName = saveFileName;
        this.price = price;
        this.remaining = remaining;
        this.category = category;
        this.year = year;
        this.good = good;
    }
}
