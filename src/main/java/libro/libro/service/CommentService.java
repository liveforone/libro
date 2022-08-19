package libro.libro.service;

import libro.libro.domain.Comment;
import libro.libro.dto.CommentDto;
import libro.libro.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    //== 댓글 리스트 ==//
    @Transactional(readOnly = true)
    public List<Comment> getCommentList(String title) {
        return commentRepository.findByItemTitle(title);
    }

    //== 댓글 등록 ==//
    @Transactional
    public void saveComment(CommentDto commentDto, String user, String title) {
        commentDto.setId(null);
        /*
        null을 넣어준다.
        save되어서 쿼리가 나갈때 jpa는 id값이 없어서 새로운 객체로 인식하고
        업데이트 되지 않고 저장된다.
         */
        commentDto.setUser(user);
        commentDto.setItemTitle(title);
        commentRepository.save(commentDto.toEntity());
    }

    //== 댓글 삭제 ==//
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
