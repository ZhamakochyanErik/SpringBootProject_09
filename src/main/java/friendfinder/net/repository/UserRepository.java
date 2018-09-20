package friendfinder.net.repository;

import friendfinder.net.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select size(u.fromList) from User u where u.id=:userId")
    int countBookmark(@Param("userId") int userId);

    @Query("select u.fromList from User u where u.id=:userId")
    List<User> findBookmark(@Param("userId") int userId, Pageable pageable);

    boolean existsByIdAndFromList_Id(int id,int userId);

    int countByNameContainsAndIdNotIn(String name,int id);

    List<User> findAllByNameContainsAndIdNotIn(String name,int userId,Pageable pageable);
}