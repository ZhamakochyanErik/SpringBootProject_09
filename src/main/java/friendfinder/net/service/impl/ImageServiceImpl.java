package friendfinder.net.service.impl;

import friendfinder.net.exception.BadRequestException;
import friendfinder.net.model.User;
import friendfinder.net.model.UserImage;
import friendfinder.net.repository.UserImageRepository;
import friendfinder.net.repository.UserRepository;
import friendfinder.net.service.ImageService;
import friendfinder.net.util.ImageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserImageRepository userImageRepository;

    @Autowired
    private ImageUtil imageUtil;

    @Transactional(rollbackFor = Exception.class)
    public void addProfileImage(MultipartFile image, User user) {
        String imgUrl = System.currentTimeMillis() + image.getOriginalFilename();
        userImageRepository.save(UserImage
                .builder()
                .imgUrl(user.getId() + "/" + imgUrl)
                .user(user)
                .build());
        user.setProfileImg(user.getId() + "/" + imgUrl);
        userRepository.save(user);
        try {
            imageUtil.save("users\\" + user.getId(),imgUrl,image);
            LOGGER.debug("user profile image saved");
        }catch (Exception e){
            imageUtil.delete("users\\" + user.getProfileImg());
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addCoverImage(MultipartFile image, User user) {
        String imgUrl = System.currentTimeMillis() + image.getOriginalFilename();
        userImageRepository.save(UserImage
                .builder()
                .imgUrl(user.getId() + "/" + imgUrl)
                .user(user)
                .build());
        user.setCoverImg(user.getId() + "/" + imgUrl);
        userRepository.save(user);
        try {
            imageUtil.save("users\\" + user.getId(),imgUrl,image);
            LOGGER.debug("user cover image saved");
        }catch (Exception e){
            imageUtil.delete("users\\" + user.getCoverImg());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserImage> getAllByUserId(int userId, Pageable pageable) {
        return userImageRepository.findAllByUserIdOrderByIdDesc(userId,pageable);
    }

    @Override
    public int countByUserId(int userId) {
        return userImageRepository.countByUserId(userId);
    }

    @Transactional
    public void changeProfileImage(User user, int imgId) {
        Optional<UserImage> image = userImageRepository.findById(imgId);
        if(!image.isPresent()){
            throw new BadRequestException();
        }
        user.setProfileImg(image.get().getImgUrl());
        userRepository.save(user);
        LOGGER.debug("profile image changed");
    }

    @Transactional
    public void changeCoverImage(User user, int imgId) {
        Optional<UserImage> image = userImageRepository.findById(imgId);
        if(!image.isPresent()){
            throw new BadRequestException();
        }
        user.setCoverImg(image.get().getImgUrl());
        userRepository.save(user);
        LOGGER.debug("cover image changed");
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id, User user) {
        Optional<UserImage> optionalImage = userImageRepository.findById(id);
        if(optionalImage.isPresent() && optionalImage.get().getUser().getId() == user.getId()){
            UserImage img = optionalImage.get();
            if(img.getImgUrl().equals(user.getProfileImg())){
                user.setProfileImg(null);
            }else if(img.getImgUrl().equals(user.getCoverImg())){
                user.setCoverImg(null);
            }
            imageUtil.delete("users\\" + img.getImgUrl());
            userImageRepository.delete(img);
            userRepository.save(user);
            LOGGER.debug("image deleted");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(MultipartFile image, User user) {
        String imgUrl = System.currentTimeMillis() + image.getOriginalFilename();
        userImageRepository.save(UserImage
                .builder()
                .imgUrl(user.getId() + "/" + imgUrl)
                .user(user)
                .build());
        try {
            imageUtil.save("users\\" + user.getId(),imgUrl,image);
            LOGGER.debug("image saved");
        }catch (Exception e){
            imageUtil.delete("users\\" + user.getId() + "\\" + imgUrl);
            throw new RuntimeException(e);
        }
    }
}
