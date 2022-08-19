package libro.libro.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String itemTitle;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public Comment(Long id, String user, String content, String itemTitle) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.itemTitle = itemTitle;
    }
}
