package friendfinder.net.repository;

import friendfinder.net.model.UserImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserImageRepository extends JpaRepository<UserImage,Integer> {

    int countByUserId(int userId);

    List<UserImage> findAllByUserIdOrderByIdDesc(int userId, Pageable pageable);
}
