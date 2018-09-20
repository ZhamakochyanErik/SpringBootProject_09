package friendfinder.net.repository;

import friendfinder.net.model.PostDislikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDislikeRepository extends JpaRepository<PostDislikes,Integer> {

    int countByPostId(int postId);

    boolean existsByUserIdAndPostId(int userId,int postId);

    PostDislikes findByPostIdAndUserId(int postId, int userId);
}