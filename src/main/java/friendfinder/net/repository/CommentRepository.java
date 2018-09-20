package friendfinder.net.repository;

import friendfinder.net.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    int countByPostId(int postId);

    List<Comment> findAllByParentIsNullAndPostId(int postId, Pageable pageable);

    List<Comment> findAllByParentId(int parentId);
}
