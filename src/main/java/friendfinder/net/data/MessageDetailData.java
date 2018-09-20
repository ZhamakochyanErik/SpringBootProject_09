package friendfinder.net.data;

import friendfinder.net.model.User;
import friendfinder.net.model.enums.MessageStatus;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDetailData {

    private User user;

    private String message;

    private String imgUrl;

    private MessageStatus status;

    private Date sendDate;
}