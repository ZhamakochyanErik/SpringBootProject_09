package friendfinder.net.data;

import friendfinder.net.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageData {

    private User user;

    private List<MessageDetailData> message;

    private Set<MessageUserData> users;
}