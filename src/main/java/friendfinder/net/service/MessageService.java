package friendfinder.net.service;

import friendfinder.net.data.MessageData;
import friendfinder.net.dto.MessageResponseDto;
import friendfinder.net.model.Message;
import friendfinder.net.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessageService {

    List<MessageResponseDto> getTop10NewMessagesByToId(int toId);

    MessageData getMessageData(User currentUser,User user);

    MessageResponseDto add(Message message, MultipartFile image,boolean imageExists);

    List<MessageResponseDto> getAllNewMessages(User currentUser,int userId);
}