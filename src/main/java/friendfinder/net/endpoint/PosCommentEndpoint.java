package friendfinder.net.endpoint;

import friendfinder.net.config.security.CurrentUser;
import friendfinder.net.dto.CommentRequestDto;
import friendfinder.net.model.Comment;
import friendfinder.net.model.Post;
import friendfinder.net.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class PosCommentEndpoint {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CommentService commentService;

    @GetMapping("/post/{postId}/comments/page/{page}")
    public ResponseEntity getComments(@PathVariable("postId")int postId,
                                      @PathVariable("page")int page){
        LOGGER.debug("postId : {},page : {}",postId,page);
        return ResponseEntity
                .ok(commentService.getComments(postId,PageRequest.of(page,4,
                        Sort.Direction.DESC,"id")));
    }

    @PostMapping("/post/comment")
    public ResponseEntity add(@RequestBody CommentRequestDto commentRequestDto,
                              @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("commentDto : {}",commentRequestDto);
        Comment comment = Comment
                .builder()
                .comment(commentRequestDto.getComment())
                .parent(commentRequestDto.getParentId() == 0 ? null : Comment
                        .builder()
                        .id(commentRequestDto.getParentId())
                        .build())
                .post(Post
                        .builder()
                        .id(commentRequestDto.getPostId())
                        .build())
                .sendDate(new Date())
                .user(currentUser.getUser())
                .build();
        return ResponseEntity
                .ok(commentService.add(comment));
    }
}