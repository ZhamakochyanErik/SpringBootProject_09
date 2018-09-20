package friendfinder.net.repository;

import friendfinder.net.model.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikesRepository extends JpaRepository<PostLikes,Integer> {

    int countByPostId(int postId);

    boolean existsByUserIdAndPostId(int userId,int postId);

    PostLikes findByPostIdAndUserId(int postId,int userId);
}