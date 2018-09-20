package friendfinder.net.service.impl;

import friendfinder.net.data.FriendData;
import friendfinder.net.data.UserProfileData;
import friendfinder.net.dto.IntegerDto;
import friendfinder.net.exception.BadRequestException;
import friendfinder.net.model.FriendRequest;
import friendfinder.net.model.User;
import friendfinder.net.model.enums.RequestStatus;
import friendfinder.net.repository.FriendRequestRepository;
import friendfinder.net.repository.UserImageRepository;
import friendfinder.net.repository.UserRepository;
import friendfinder.net.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserImageRepository userImageRepository;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        LOGGER.debug("user saved");
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void update(User user) {
        userRepository.save(user);
        LOGGER.debug("user updated");
    }

    @Override
    public UserProfileData getUserProfileDataByUserId(int userId) {
        List<FriendRequest> friendRequestList = friendRequestRepository.findAllByFromIdOrToIdAndRequestStatus(userId,
                RequestStatus.ACCEPTED, PageRequest.of(0, 4));
        List<User> friendsList = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequestList) {
            if(friendRequest.getFrom().getId() != userId){
                friendsList.add(friendRequest.getFrom());
            }else {
                friendsList.add(friendRequest.getTo());
            }
        }
        return UserProfileData
                .builder()
                .friendsCount(friendRequestRepository.countByFromIdOrToIdAndRequestStatus(userId,RequestStatus.ACCEPTED))
                .imagesCount(userImageRepository.countByUserId(userId))
                .friends(friendsList)
                .build();
    }

    @Override
    public Optional<User> getById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public List<FriendData> getFriendsByUserId(int userId, Pageable pageable) {
        List<FriendData> friendDataList = new ArrayList<>();
        List<FriendRequest> friendRequestList = friendRequestRepository
                .findAllByFromIdOrToIdAndRequestStatus(userId, RequestStatus.ACCEPTED, pageable);
        for (FriendRequest friendRequest : friendRequestList) {
            User friend;
            if(friendRequest.getFrom().getId() == userId){
                friend = friendRequest.getTo();
            }else {
                friend = friendRequest.getFrom();
            }
            friendDataList.add(FriendData
                    .builder()
                    .friend(friend)
                    .friendsCount(friendRequestRepository
                            .countByFromIdOrToIdAndRequestStatus(friend.getId(),RequestStatus.ACCEPTED))
                    .imageCount(userImageRepository.countByUserId(friend.getId()))
                    .build());
        }
        return friendDataList;
    }

    @Override
    public int countFriendsByUserId(int userId) {
        return friendRequestRepository.countByFromIdOrToIdAndRequestStatus(userId,RequestStatus.ACCEPTED);
    }

    @Override
    public int countBookmarkByUserId(int userId) {
        return userRepository.countBookmark(userId);
    }

    @Override
    public List<FriendData> getBookmarkByUserId(int userId, Pageable pageable) {
        List<FriendData> friendDataList = new ArrayList<>();
        List<User> users = userRepository.findBookmark(userId,pageable);
        for (User user: users) {
            friendDataList.add(FriendData
                    .builder()
                    .friend(user)
                    .friendsCount(friendRequestRepository
                            .countByFromIdOrToIdAndRequestStatus(user.getId(),RequestStatus.ACCEPTED))
                    .imageCount(userImageRepository.countByUserId(user.getId()))
                    .build());
        }
        return friendDataList;
    }

    @Transactional
    public IntegerDto addBookmark(User currentUser, int userId) {
        int status;
        currentUser = userRepository.save(currentUser);
        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            throw new BadRequestException();
        }
        if(currentUser.getFromList().contains(optionalUser.get())){
            status = 201;
            currentUser.getFromList().remove(optionalUser.get());
        }else {
            status = 200;
            currentUser.getFromList().add(optionalUser.get());
        }
        userRepository.save(currentUser);
        return IntegerDto
                .builder()
                .count(status)
                .build();
    }

    @Override
    public int searchCount(String name,int userId) {
        return userRepository.countByNameContainsAndIdNotIn(name,userId);
    }

    @Override
    public List<User> searchResult(String name,int userId, Pageable pageable) {
        return userRepository.findAllByNameContainsAndIdNotIn(name,userId,pageable);
    }

}
