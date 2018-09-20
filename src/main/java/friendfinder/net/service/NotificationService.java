package friendfinder.net.service;

import friendfinder.net.dto.NotificationResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    NotificationResponseDto getTop10ByNewNotByUserId(int userId);

    int updateStatus(List<Integer> notificationList);

    NotificationResponseDto getAllByUserId(int userId, Pageable pageable);
}