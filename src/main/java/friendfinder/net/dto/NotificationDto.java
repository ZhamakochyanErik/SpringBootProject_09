package friendfinder.net.dto;

import friendfinder.net.model.enums.NotificationStatus;
import friendfinder.net.model.enums.NotificationType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {

    private int id;

    private NotificationType type;

    private NotificationStatus status;

    private UserDto from;

    private PostDto post;

    private String date;
}