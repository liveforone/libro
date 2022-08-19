package libro.libro.controller;

import libro.libro.domain.Comment;
import libro.libro.domain.Item;
import libro.libro.dto.CommentDto;
import libro.libro.service.CommentService;
import libro.libro.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final ItemService itemService;

    //== 댓글 리스트 ==//
    @GetMapping("/user/comment/{title}")
    public String commentList(
            @PathVariable("title") String title,
            Model model,
            Principal principal
    ) {
        String member = principal.getName();
        List<Comment> commentList = commentService.getCommentList(title);
        Item item = itemService.getItem(title);
        model.addAttribute("commentList", commentList);
        model.addAttribute("item", item);
        model.addAttribute("member", member);
        return "/comment/commentList";
    }

    //== 댓글 작성 ==//
    @PostMapping("/user/comment/post/{title}")
    public String commentPost(
            @PathVariable("title") String title,
            CommentDto commentDto,
            Principal principal
    ) {
        String url = "/user/comment/" + title;
        String user = principal.getName();
        commentService.saveComment(commentDto, user, title);
        log.info("Comment Posting Success!!");
        return "redirect:" + url;
    }

    //== 댓글 삭제 ==//
    @PostMapping("/user/comment/delete/{id}")
    public String deleteComment(
            @PathVariable("id") Long id,
            @RequestParam("itemTitle") String itemTitle
    ) {
        String url = "/user/comment/" + itemTitle;
        commentService.deleteComment(id);
        log.info("Comment Deletion Successful!!");
        return "redirect:" + url;
    }
}
