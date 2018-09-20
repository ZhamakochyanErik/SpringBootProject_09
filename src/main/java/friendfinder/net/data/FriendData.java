package friendfinder.net.data;

import friendfinder.net.model.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendData {

    private User friend;

    private int imageCount;

    private int friendsCount;
}