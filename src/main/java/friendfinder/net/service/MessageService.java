package friendfinder.net.service;

import friendfinder.net.dto.MessageRequestDto;

import java.util.List;

public interface MessageService {

    List<MessageRequestDto> getTop10NewMessagesByToId(int toId);
}
