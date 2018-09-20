package friendfinder.net.controller;

import friendfinder.net.config.security.CurrentUser;
import friendfinder.net.form.PostRequestForm;
import friendfinder.net.model.Post;
import friendfinder.net.model.User;
import friendfinder.net.service.PostService;
import friendfinder.net.util.ImageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class PostController implements Pages {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private PostService postService;

    @Autowired
    private ImageUtil imageUtil;

    @GetMapping("/posts/page/{page}")
    public @ResponseBody
    ResponseEntity loadPosts(@PathVariable("page") int page,
                             @AuthenticationPrincipal CurrentUser user) {
        LOGGER.debug("page : {}", page);
        return ResponseEntity
                .ok(postService.getAllByUserId(user.getUser(), PageRequest.of(page, 6,
                        Sort.Direction.DESC, "id")));
    }

    @GetMapping("/post/{postId}/delete")
    public String deletePost(@PathVariable("postId") String postId, @AuthenticationPrincipal CurrentUser user) {
        LOGGER.debug("postId : {}", postId);
        try {
            postService.deletePost(user.getUser().getId(), Integer.parseInt(postId));
        } catch (NumberFormatException e) {
        }
        return "redirect:/";
    }

    @GetMapping("/user/{userId}/posts/page/{page}")
    public @ResponseBody
    ResponseEntity userPosts(@PathVariable("page") int page,
                             @PathVariable("userId")int userId,@AuthenticationPrincipal CurrentUser currentUser) {
        LOGGER.debug("userId : {},page : {}", page,userId);
        return ResponseEntity
                .ok(postService.getAllUserPosts(userId,currentUser.getUser().getId(),PageRequest.of(page, 6,
                        Sort.Direction.DESC, "id")));
    }

    @PostMapping("/post/add")
    public String add(PostRequestForm form, @AuthenticationPrincipal CurrentUser user) {
        LOGGER.debug("form : {}", form);
        if (!isValid(form.getTitle()) && !isValid(form.getDescription()) &&
                (form.getImage().isEmpty() || !imageUtil.isValidFormat(form.getImage().getContentType()))) {
            return "redirect:/?postError";
        } else {
            boolean existsImage = !form.getImage().isEmpty() && imageUtil.isValidFormat(form.getImage().getContentType());
            Post post = Post
                    .builder()
                    .title(form.getTitle() == null || form.getTitle().length() == 0 ? null : form.getTitle())
                    .description(form.getDescription() == null || form.getDescription().length() == 0 ? null : form.getDescription())
                    .createdDate(new Date())
                    .imgUrl(existsImage ? "" : null)
                    .user(user.getUser())
                    .build();
            postService.add(post, form.getImage(), existsImage);
            return "redirect:/";
        }
    }

    @GetMapping("/post/{id}")
    public String getById(@PathVariable("id") String strId, Model model,
                          @RequestAttribute("user")User user) {
        LOGGER.debug("postId : {}", strId);
        try {
            model.addAttribute("postData", postService.getPageDataById(Integer.parseInt(strId),user));
            return POST_DETAILS;
        } catch (NumberFormatException e) {
        }
        return "redirect:/";
    }

    @PostMapping("/user/post/update")
    public String update(PostRequestForm form,
                         @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("form : {}", form);
        if (!isValid(form.getTitle()) && !isValid(form.getDescription()) &&
                (form.getImage().isEmpty() || !imageUtil.isValidFormat(form.getImage().getContentType()))) {
            return "redirect:/post/" + form.getId() +"?postError";
        } else {
            boolean existsImage = !form.getImage().isEmpty() && imageUtil.isValidFormat(form.getImage().getContentType());
            Post persitsPost = postService.getById(form.getId()).get();
            Post post = Post
                    .builder()
                    .id(form.getId())
                    .title(form.getTitle() == null || form.getTitle().length() == 0 ? null : form.getTitle())
                    .description(form.getDescription() == null || form.getDescription().length() == 0 ? null : form.getDescription())
                    .createdDate(persitsPost.getCreatedDate())
                    .imgUrl(existsImage ? persitsPost.getImgUrl() != null ? persitsPost.getImgUrl().split("/")[1] :
                            System.currentTimeMillis() + form.getImage().getOriginalFilename(): persitsPost.getImgUrl() != null ?"" : null)
                    .user(currentUser.getUser())
                    .build();
            postService.update(post, form.getImage(), existsImage);
            return "redirect:/post/" + form.getId() +"?success";
        }
    }

    private boolean isValid(String param) {
        return param != null && param.length() > 0;
    }
}