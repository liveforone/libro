package libro.libro.repository;

import libro.libro.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //== 댓글 리스트 ==//
    List<Comment> findByItemTitle(String title);
}
