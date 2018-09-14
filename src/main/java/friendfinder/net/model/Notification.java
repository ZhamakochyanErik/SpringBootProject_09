package friendfinder.net.model;

import friendfinder.net.model.enums.NotificationStatus;
import friendfinder.net.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    @ManyToOne
    private User to;

    @ManyToOne
    private User from;

    @ManyToOne
    private Post post;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "notification_date")
    private Date date;
}