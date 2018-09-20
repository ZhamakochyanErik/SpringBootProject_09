package friendfinder.net.data;

import friendfinder.net.model.User;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileData {

    private int imagesCount;

    private int friendsCount;

    private List<User> friends;
}