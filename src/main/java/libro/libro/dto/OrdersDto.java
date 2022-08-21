package libro.libro.dto;

import libro.libro.domain.Orders;
import libro.libro.domain.OrderStatus;
import lombok.Builder;
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

    @Builder  //따로 뷰에서 정보를 가져오지않고 직접 넣어야해서 빌더 생성함.
    public OrdersDto(Long id, String itemTitle, String member, OrderStatus status) {
        this.id = id;
        this.itemTitle = itemTitle;
        this.member = member;
        this.status = status;
    }
}
