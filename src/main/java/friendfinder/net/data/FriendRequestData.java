package friendfinder.net.data;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestData {
    
    private boolean accepted;
    
    private boolean youSend;

    private boolean userSend;

    private boolean bookmarkExists;

    private boolean lastCase;

}