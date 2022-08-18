package libro.libro.dto;

import libro.libro.domain.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {

    private Long id;
    private String title;
    private String author;
    private String writer;
    private String saveFileName;
    private int price;
    private int remaining;
    private String category;
    private String year;
    private int good;

    public Item toEntity() {
        return Item.builder()
                .id(id)
                .title(title)
                .author(author)
                .writer(writer)
                .saveFileName(saveFileName)
                .price(price)
                .remaining(remaining)
                .category(category)
                .year(year)
                .good(good)
                .build();
    }
}
