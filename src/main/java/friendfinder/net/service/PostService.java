package friendfinder.net.service;

import friendfinder.net.data.PostData;
import friendfinder.net.dto.IntegerDto;
import friendfinder.net.model.Post;
import friendfinder.net.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<PostData> getAllByUserId(User user, Pageable pageable);

    List<PostData> getAllUserPosts(int userId,int curretnUserId, Pageable pageable);

    IntegerDto addLike(int postId,int userId);

    IntegerDto addDislike(int postId,int userId);

    void deletePost(int userId,int postId);

    void add(Post post, MultipartFile image,boolean existsImage);

    PostData getPageDataById(int id, User user);

    Optional<Post> getById(int id);

    void update(Post post, MultipartFile image,boolean existsImage);
}