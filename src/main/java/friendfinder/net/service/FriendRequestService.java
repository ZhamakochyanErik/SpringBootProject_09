package friendfinder.net.service;

import friendfinder.net.data.FriendRequestData;
import friendfinder.net.model.FriendRequest;

public interface FriendRequestService {

    void add(FriendRequest friendRequest);

    boolean isExistsByToIdOrFromId(int toId,int fromId);

    FriendRequestData getRequestData(int currentUserId, int userId);

    void delete(int toId,int fromId);

    void accept(int toId,int fromId);
}