package friendfinder.net.repository;

import friendfinder.net.model.Notification;
import friendfinder.net.model.enums.NotificationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {

    List<Notification> findAllByToIdAndNotificationStatusOrderByDateDesc(int toId, NotificationStatus status, Pageable pageable);
}
