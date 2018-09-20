package friendfinder.net.service.impl;

import friendfinder.net.data.MessageData;
import friendfinder.net.data.MessageDetailData;
import friendfinder.net.data.MessageUserData;
import friendfinder.net.dto.MessageResponseDto;
import friendfinder.net.dto.UserDto;
import friendfinder.net.model.FriendRequest;
import friendfinder.net.model.Message;
import friendfinder.net.model.User;
import friendfinder.net.model.enums.MessageStatus;
import friendfinder.net.model.enums.RequestStatus;
import friendfinder.net.repository.FriendRequestRepository;
import friendfinder.net.repository.MessageRepository;
import friendfinder.net.service.MessageService;
import friendfinder.net.util.ImageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private ImageUtil imageUtil;

    @Override
    public List<MessageResponseDto> getTop10NewMessagesByToId(int toId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        List<Message> messages = messageRepository.findAllByToIdAndMessageStatusOrderBySendDateDesc(toId, MessageStatus.NEW);
        List<MessageResponseDto> messageRequestList = new ArrayList<>();
        for (Message message : messages) {
            messageRequestList
                    .add(getResponseDto(message,dateFormat));
        }
        return messageRequestList;
    }

    @Transactional
    public MessageData getMessageData(User currentUser, User user) {
        Set<MessageUserData> users = new LinkedHashSet<>();
        List<MessageDetailData> messageDetailList = new ArrayList<>();
        if(user != null){
            users.add(MessageUserData.
                    builder()
                    .user(user)
                    .build());
            List<Message> messages = messageRepository.findAllByToIdAndFromId(currentUser.getId(), user.getId());
            for (Message message : messages) {
                if(message.getTo().getId() == currentUser.getId() && message.getMessageStatus() == MessageStatus.NEW){
                    message.setMessageStatus(MessageStatus.VIEWED);
                    messageRepository.save(message);
                }
                messageDetailList.add(MessageDetailData
                        .builder()
                        .message(message.getMessage())
                        .imgUrl(message.getImgUrl())
                        .sendDate(message.getSendDate())
                        .user(message.getFrom())
                        .status(message.getMessageStatus())
                        .build());
            }
        }
        List<FriendRequest> friendRequests = friendRequestRepository
                .findAllByFromIdOrToIdAndRequestStatus(currentUser.getId(),RequestStatus.ACCEPTED);
        List<Message> messages = messageRepository.findAllMessages(currentUser.getId());
        for (Message message : messages) {
            if(message.getFrom().getId() == currentUser.getId()){
                users.add(MessageUserData
                        .builder()
                        .user(message.getTo())
                        .count(messageRepository
                                .countByFromIdAndToIdAndMessageStatus(
                                        message.getTo().getId(),currentUser.getId(),MessageStatus.NEW))
                        .build());
            }else {
                users.add(MessageUserData
                        .builder()
                        .user(message.getFrom())
                        .count(messageRepository
                                .countByFromIdAndToIdAndMessageStatus(
                                        message.getFrom().getId(),currentUser.getId(),MessageStatus.NEW))
                        .build());
            }
        }
        for (FriendRequest friendRequest : friendRequests) {
            if(friendRequest.getFrom().getId() == currentUser.getId()){
                users.add(MessageUserData
                        .builder()
                        .user(friendRequest.getTo())
                        .count(messageRepository
                                .countByFromIdAndToIdAndMessageStatus(
                                        friendRequest.getTo().getId(),currentUser.getId(),MessageStatus.NEW))
                        .build());
            }else {
                users.add(MessageUserData
                        .builder()
                        .user(friendRequest.getFrom())
                        .count(messageRepository
                                .countByFromIdAndToIdAndMessageStatus(
                                        friendRequest.getFrom().getId(),currentUser.getId(),MessageStatus.NEW))
                        .build());
            }
        }
        if(user == null){
            for (MessageUserData userData : users) {
                user = userData.getUser();
                break;
            }
            if(user != null){
                for (Message message : messageRepository.findAllByToIdAndFromId(currentUser.getId(),user.getId())) {
                    if(message.getTo().getId() == currentUser.getId() && message.getMessageStatus() == MessageStatus.NEW){
                        message.setMessageStatus(MessageStatus.VIEWED);
                        messageRepository.save(message);
                    }
                    messageDetailList.add(MessageDetailData
                            .builder()
                            .message(message.getMessage())
                            .imgUrl(message.getImgUrl())
                            .sendDate(message.getSendDate())
                            .user(message.getFrom())
                            .status(message.getMessageStatus())
                            .build());
                }
            }
        }
        return MessageData
                .builder()
                .user(user)
                .message(messageDetailList)
                .users(users)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public MessageResponseDto add(Message message, MultipartFile image, boolean imageExists) {
        messageRepository.save(message);
        if(imageExists){
            String imgUrl = System.currentTimeMillis() + image.getOriginalFilename();
            message.setImgUrl(message.getId() + "/" + imgUrl);
            try {
                imageUtil.save("messages\\" + message.getId(),imgUrl,image);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        LOGGER.debug("message saved");
        return getResponseDto(message,new SimpleDateFormat("dd.MM HH:mm:ss"));
    }

    @Transactional
    public List<MessageResponseDto> getAllNewMessages(User currentUser, int userId) {
        List<Message> messages = messageRepository.findAllByFromIdAndToIdAndMessageStatus(userId, currentUser.getId(), MessageStatus.NEW);
        List<MessageResponseDto> messageResponseDtoList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm:ss");
        for (Message message : messages) {
            message.setMessageStatus(MessageStatus.VIEWED);
            messageResponseDtoList.add(getResponseDto(message,dateFormat));
        }
        messageRepository.saveAll(messages);
        return messageResponseDtoList;
    }


    private MessageResponseDto getResponseDto(Message message,SimpleDateFormat dateFormat){
        return MessageResponseDto
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
                .build();
    }
}
