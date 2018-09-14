package friendfinder.net.service.impl;

import friendfinder.net.dto.NotificationDto;
import friendfinder.net.dto.PostDto;
import friendfinder.net.dto.UserDto;
import friendfinder.net.model.Notification;
import friendfinder.net.model.enums.NotificationStatus;
import friendfinder.net.repository.NotificationRepository;
import friendfinder.net.service.NotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public List<NotificationDto> getTop10ByNewNotByUserId(int userId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        List<Notification> notifications = notificationRepository.findAllByToIdAndNotificationStatusOrderByDateDesc(userId,
                NotificationStatus.NEW, PageRequest.of(0, 10, Sort.Direction.DESC, "date"));
        int size;
        size = 10 - notifications.size();
        if (size > 0) {
            notifications.addAll(notificationRepository.findAllByToIdAndNotificationStatusOrderByDateDesc(userId,
                    NotificationStatus.VIEWED, PageRequest.of(0, size, Sort.Direction.DESC, "date")));
        }
        for (Notification notification : notifications) {
            notificationDtoList.add(NotificationDto
                    .builder()
                    .id(notification.getId())
                    .status(notification.getNotificationStatus())
                    .type(notification.getNotificationType())
                    .date(dateFormat.format(notification.getDate()))
                    .from(notification.getFrom() == null ? null : UserDto
                            .builder()
                            .id(notification.getFrom().getId())
                            .name(notification.getFrom().getName())
                            .surname(notification.getFrom().getSurname())
                            .email(notification.getFrom().getEmail())
                            .coverImg(notification.getFrom().getCoverImg())
                            .profileImg(notification.getFrom().getProfileImg())
                            .age(notification.getFrom().getAge())
                            .city(notification.getFrom().getCity())
                            .build())
                    .post(notification.getPost() == null ? null : PostDto
                            .builder()
                            .id(notification.getPost().getId())
                            .title(notification.getPost().getTitle())
                            .description(notification.getPost().getDescription())
                            .createdDate(dateFormat.format(notification.getPost().getCreatedDate()))
                            .imgUrl(notification.getPost().getImgUrl())
                            .build())
                    .build());
        }
        return notificationDtoList;
    }

    @Transactional
    public void updateStatus(List<Integer> notificationList) {
        List<Notification> notifications = notificationRepository.findAllById(notificationList);
        for (Notification notification : notifications) {
            if(notification.getNotificationStatus() == NotificationStatus.NEW){
                notification.setNotificationStatus(NotificationStatus.VIEWED);
            }
        }
        LOGGER.debug("notifications status updated");
    }
}
