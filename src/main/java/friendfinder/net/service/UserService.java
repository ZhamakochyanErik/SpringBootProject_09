package friendfinder.net.service;

import friendfinder.net.data.FriendData;
import friendfinder.net.data.UserProfileData;
import friendfinder.net.dto.IntegerDto;
import friendfinder.net.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    boolean existsByEmail(String email);

    void add(User user);

    Optional<User> getByEmail(String email);

    void update(User user);

    UserProfileData getUserProfileDataByUserId(int userId);

    Optional<User> getById(int id);

    List<FriendData> getFriendsByUserId(int userId, Pageable pageable);

    int countFriendsByUserId(int userId);

    int countBookmarkByUserId(int userId);

    List<FriendData> getBookmarkByUserId(int userId,Pageable pageable);

    IntegerDto addBookmark(User currentUser,int userId);

    int searchCount(String name,int userId);

    List<User> searchResult(String name,int userId,Pageable pageable);
}