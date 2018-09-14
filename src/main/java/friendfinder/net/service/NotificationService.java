package friendfinder.net.service;

import friendfinder.net.dto.NotificationDto;

import java.util.List;

public interface NotificationService {

    List<NotificationDto> getTop10ByNewNotByUserId(int userId);

    void updateStatus(List<Integer> notificationList);
}