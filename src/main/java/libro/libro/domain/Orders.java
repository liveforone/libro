package libro.libro.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String itemTitle;
    private String member;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status; //ORDER, CANCEL

    @CreatedDate
    private LocalDate createdDate;  //plusDays(5)로 5일후 날짜 얻을 수 있음.

    @Builder
    public Orders(Long id, String itemTitle, String member, OrderStatus status) {
        this.id = id;
        this.itemTitle = itemTitle;
        this.member = member;
        this.status = status;
    }
}
