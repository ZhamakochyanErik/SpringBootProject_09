package friendfinder.net.repository;

import friendfinder.net.model.Message;
import friendfinder.net.model.enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {

    List<Message> findAllByToIdAndMessageStatusOrderBySendDateDesc(int toId, MessageStatus status);

    @Query("select m from Message m where m.to.id=:userId or m.from.id=:userId")
    List<Message> findAllMessages(@Param("userId")int userId);

    @Query("select m from Message m where (m.to.id=:toId and m.from.id=:fromId) or (m.to.id=:fromId and m.from.id=:toId) order by m.id asc ")
    List<Message> findAllByToIdAndFromId(@Param("toId") int toId,@Param("fromId") int fromId);

    int countByFromIdAndToIdAndMessageStatus(int fromId,int toId,MessageStatus status);

    List<Message> findAllByFromIdAndToIdAndMessageStatus(int fromId,int toId,MessageStatus status);
}