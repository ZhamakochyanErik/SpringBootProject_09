package friendfinder.net.data;

import friendfinder.net.model.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageUserData {

    private User user;

    private int count;
}