package libro.libro.dto;

import libro.libro.domain.Orders;
import libro.libro.domain.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class OrdersDto {

    private Long id;
    private String itemTitle;
    private String member;
    private OrderStatus status;
    private LocalDate createdDate;

    public Orders toEntity() {
        return Orders.builder()
                .id(id)
                .itemTitle(itemTitle)
                .member(member)
                .status(status)
                .build();
    }
}
