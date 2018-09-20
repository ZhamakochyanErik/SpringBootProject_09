package friendfinder.net.repository;

import friendfinder.net.model.Post;
import friendfinder.net.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {

    List<Post> findAllByUserIn(List<User> users, Pageable pageable);

    List<Post> findAllByUserId(int userId, Pageable pageable);

    boolean existsByUserIdAndId(int userId,int postId);
}