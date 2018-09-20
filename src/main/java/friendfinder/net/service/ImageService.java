package friendfinder.net.service;

import friendfinder.net.model.User;
import friendfinder.net.model.UserImage;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    void addProfileImage(MultipartFile image, User user);

    void addCoverImage(MultipartFile image,User user);

    List<UserImage> getAllByUserId(int userId, Pageable pageable);

    int countByUserId(int userId);

    void changeProfileImage(User user,int imgId);

    void changeCoverImage(User user,int imgId);

    void deleteById(int id,User user);

    void add(MultipartFile image,User user);
}