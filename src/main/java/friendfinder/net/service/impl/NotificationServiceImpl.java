package friendfinder.net.service.impl;

import friendfinder.net.dto.NotificationDto;
import friendfinder.net.dto.NotificationResponseDto;
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
import org.springframework.data.domain.Pageable;
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
    public NotificationResponseDto getTop10ByNewNotByUserId(int userId) {
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        List<Notification> notifications = notificationRepository.
                findAllByToIdOrderByIdDesc(userId,PageRequest.of(0, 10));
        for (Notification notification : notifications) {
            notificationDtoList.add(getDto(notification));
        }
        return NotificationResponseDto
                .builder()
                .notifications(notificationDtoList)
                .count(notificationRepository.countByToIdAndNotificationStatus(userId,NotificationStatus.NEW))
                .build();
    }

    @Transactional
    public int updateStatus(List<Integer> notificationList) {
        int count = 0;
        List<Notification> notifications = notificationRepository.findAllById(notificationList);
        for (Notification notification : notifications) {
            if(notification.getNotificationStatus() == NotificationStatus.NEW){
                notification.setNotificationStatus(NotificationStatus.VIEWED);
                count++;
            }
        }
        notificationRepository.flush();
        LOGGER.debug("notifications status updated");
        return count;
    }

    @Transactional
    public NotificationResponseDto getAllByUserId(int userId, Pageable pageable) {
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        List<Notification> notifications = notificationRepository.
                findAllByToIdOrderByIdDesc(userId, PageRequest.of(pageable.getPageNumber(), 10));
        int count = 0;
        for (Notification notification : notifications) {
            if(notification.getNotificationStatus() == NotificationStatus.NEW){
                notification.setNotificationStatus(NotificationStatus.VIEWED);
                count++;
            }
            notificationDtoList.add(getDto(notification));
        }
        notificationRepository.flush();
        return NotificationResponseDto
                .builder()
                .notificationExists(notificationDtoList.size() == 10)
                .notifications(notificationDtoList)
                .count(count)
                .build();
    }

    private NotificationDto getDto(Notification notification){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM_HH:mm");
        return
                NotificationDto
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
                        .build();
    }
}