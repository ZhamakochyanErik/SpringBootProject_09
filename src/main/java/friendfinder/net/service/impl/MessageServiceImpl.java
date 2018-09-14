package friendfinder.net.service.impl;

import friendfinder.net.dto.MessageRequestDto;
import friendfinder.net.dto.UserDto;
import friendfinder.net.model.Message;
import friendfinder.net.model.enums.MessageStatus;
import friendfinder.net.repository.MessageRepository;
import friendfinder.net.service.MessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<MessageRequestDto> getTop10NewMessagesByToId(int toId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        List<Message> messages = messageRepository.findTop10AllByToIdAndMessageStatusOrderBySendDateDesc(toId, MessageStatus.NEW);
        List<MessageRequestDto> messageRequestList = new ArrayList<>();
        for (Message message : messages) {
            messageRequestList
                    .add(MessageRequestDto
                            .builder()
                            .id(message.getId())
                            .from(UserDto
                                    .builder()
                                    .id(message.getFrom().getId())
                                    .name(message.getFrom().getName())
                                    .surname(message.getFrom().getSurname())
                                    .email(message.getFrom().getEmail())
                                    .age(message.getFrom().getAge())
                                    .city(message.getFrom().getCity())
                                    .profileImg(message.getFrom().getProfileImg())
                                    .coverImg(message.getFrom().getCoverImg())
                                    .build())
                            .imgUrl(message.getImgUrl())
                            .message(message.getMessage())
                            .sendDate(dateFormat.format(message.getSendDate()))
                            .build());
        }
        return messageRequestList;
    }
}
