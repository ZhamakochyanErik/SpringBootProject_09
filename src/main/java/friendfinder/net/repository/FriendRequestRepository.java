package friendfinder.net.repository;

import friendfinder.net.model.FriendRequest;
import friendfinder.net.model.enums.RequestStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest,Integer> {

    @Query("select f from FriendRequest f where (f.from.id=:userId or f.to.id=:userId ) and f.requestStatus=:status")
    List<FriendRequest> findAllByFromIdOrToIdAndRequestStatus(@Param("userId") int userId, @Param("status") RequestStatus status);

    @Query("select f from FriendRequest f where (f.from.id=:userId or f.to.id=:userId ) and f.requestStatus=:status")
    List<FriendRequest> findAllByFromIdOrToIdAndRequestStatus(@Param("userId") int userId, @Param("status") RequestStatus status, Pageable pageable);

    @Query("select count(*) from FriendRequest f where (f.from.id=:userId or f.to.id=:userId) and f.requestStatus=:status")
    int countByFromIdOrToIdAndRequestStatus(@Param("userId") int userId,@Param("status")RequestStatus status);

    @Query("select f from FriendRequest f where (f.to.id = :toId and f.from.id = :fromId) or (f.to.id = :fromId and f.from.id = :toId)")
    FriendRequest findByFromIdOrToId(@Param("toId")int toId,@Param("fromId")int fromId);
}