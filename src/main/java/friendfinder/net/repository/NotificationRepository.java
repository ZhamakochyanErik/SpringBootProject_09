package friendfinder.net.repository;

import friendfinder.net.model.Notification;
import friendfinder.net.model.enums.NotificationStatus;
import friendfinder.net.model.enums.NotificationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {

    List<Notification> findAllByToIdOrderByIdDesc(int toId,Pageable pageable);

    int countByToIdAndNotificationStatus(int toId, NotificationStatus status);

    @Query("select n from Notification n where (n.from.id=:fromId and n.to.id=:toId and n.notificationType=:type) or " +
            "(n.from.id=:toId and n.to.id=:fromId and n.notificationType=:type)")
    Notification findByToIdAndFromIdAndNotificationType(@Param("toId") int toId,@Param("fromId")int fromId,
                                                        @Param("type") NotificationType type);
}