package friendfinder.net.service.impl;

import friendfinder.net.data.FriendRequestData;
import friendfinder.net.model.FriendRequest;
import friendfinder.net.model.Notification;
import friendfinder.net.model.User;
import friendfinder.net.model.enums.NotificationStatus;
import friendfinder.net.model.enums.NotificationType;
import friendfinder.net.model.enums.RequestStatus;
import friendfinder.net.repository.FriendRequestRepository;
import friendfinder.net.repository.NotificationRepository;
import friendfinder.net.repository.UserRepository;
import friendfinder.net.service.FriendRequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class FriendRequestServiceImpl implements FriendRequestService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public void add(FriendRequest friendRequest) {
        friendRequestRepository.save(friendRequest);
        notificationRepository.save(Notification
                .builder()
                .notificationType(NotificationType.FRIEND_REQUEST)
                .notificationStatus(NotificationStatus.NEW)
                .to(friendRequest.getTo())
                .from(friendRequest.getFrom())
                .date(new Date())
                .build());
        LOGGER.debug("friend request saved");
    }

    @Override
    public boolean isExistsByToIdOrFromId(int toId, int fromId) {
        return friendRequestRepository.findByFromIdOrToId(toId,fromId) != null;
    }

    @Override
    public FriendRequestData getRequestData(int currentUserId, int userId) {
        boolean accepted = false;
        boolean youSend = false;
        boolean userSend = false;
        boolean lastCase = false;
        FriendRequest friendRequest = friendRequestRepository.findByFromIdOrToId(currentUserId, userId);
        if(friendRequest == null){
            lastCase = true;
        }else {
            if(friendRequest.getFrom().getId() == currentUserId){
                switch (friendRequest.getRequestStatus()){
                    case ACCEPTED:
                        accepted=true;
                        break;
                    case NEW:
                        youSend=true;
                }
            }else {
                switch (friendRequest.getRequestStatus()){
                    case ACCEPTED:
                        accepted=true;
                        break;
                    case NEW:
                        userSend=true;
                }
            }
        }
        return FriendRequestData
                .builder()
                .accepted(accepted)
                .userSend(userSend)
                .youSend(youSend)
                .lastCase(lastCase)
                .bookmarkExists(userRepository.existsByIdAndFromList_Id(currentUserId,userId))
                .build();
    }

    @Transactional
    public void delete(int toId, int fromId) {
        FriendRequest friendRequest = friendRequestRepository.findByFromIdOrToId(toId, fromId);
        if(friendRequest != null){
            friendRequestRepository.delete(friendRequest);
            Notification notification = notificationRepository
                    .findByToIdAndFromIdAndNotificationType(toId, fromId, NotificationType.FRIEND_REQUEST);
            if(notification != null){
                notificationRepository.delete(notification);
            }
            LOGGER.debug("friend request deleted");
        }
    }

    @Transactional
    public void accept(int toId, int fromId) {
        FriendRequest friendRequest = friendRequestRepository.findByFromIdOrToId(toId, fromId);
        if(friendRequest != null) {
            friendRequest.setRequestStatus(RequestStatus.ACCEPTED);
            Notification notification = notificationRepository
                    .findByToIdAndFromIdAndNotificationType(toId, fromId, NotificationType.FRIEND_REQUEST);
            if(notification != null){
                notificationRepository.delete(notification);
            }
            LOGGER.debug("friend request status updated");
        }
    }
}
