package friendfinder.net.endpoint;

import friendfinder.net.config.security.CurrentUser;
import friendfinder.net.dto.PostLikeOrDislikeRequestDto;
import friendfinder.net.service.PostService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostLikeOrDislikeEndpoint {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private PostService postService;

    @PostMapping("/post/like/add")
    public ResponseEntity addLike(@RequestBody PostLikeOrDislikeRequestDto likeDto,
                                  @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("likeDto : {}",likeDto);
        return ResponseEntity
                .ok(postService.addLike(likeDto.getPostId(),currentUser.getUser().getId()));
    }

    @PostMapping("/post/dislike/add")
    public ResponseEntity addDislike(@RequestBody PostLikeOrDislikeRequestDto likeDto,
                                  @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("likeDto : {}",likeDto);
        return ResponseEntity
                .ok(postService.addDislike(likeDto.getPostId(),currentUser.getUser().getId()));
    }
}