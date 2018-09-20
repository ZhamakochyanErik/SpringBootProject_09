package friendfinder.net.service.impl;

import friendfinder.net.dto.CommentDto;
import friendfinder.net.dto.CommentResponseDto;
import friendfinder.net.dto.UserDto;
import friendfinder.net.model.Comment;
import friendfinder.net.model.Notification;
import friendfinder.net.model.Post;
import friendfinder.net.model.enums.NotificationStatus;
import friendfinder.net.model.enums.NotificationType;
import friendfinder.net.repository.CommentRepository;
import friendfinder.net.repository.NotificationRepository;
import friendfinder.net.repository.PostRepository;
import friendfinder.net.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<CommentResponseDto> getComments(int postId, Pageable pageable) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm:ss");
        List<Comment> comments = commentRepository.findAllByParentIsNullAndPostId(postId,pageable);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            List<CommentResponseDto> childrens = new ArrayList<>();
            addChildrenList(childrens,comment.getId());
            commentResponseDtoList.add(CommentResponseDto
                    .builder()
                    .commentDto(CommentDto
                            .builder()
                            .id(comment.getId())
                            .comment(comment.getComment())
                            .sendDate(dateFormat.format(comment.getSendDate()))
                            .user(UserDto
                                    .builder()
                                    .id(comment.getUser().getId())
                                    .name(comment.getUser().getName())
                                    .surname(comment.getUser().getSurname())
                                    .email(comment.getUser().getEmail())
                                    .coverImg(comment.getUser().getCoverImg())
                                    .profileImg(comment.getUser().getProfileImg())
                                    .age(comment.getUser().getAge())
                                    .city(comment.getUser().getCity())
                                    .build())
                            .build())
                    .childrens(childrens)
                    .build());
        }
        return commentResponseDtoList;
    }

    @Transactional
    public CommentDto add(Comment comment) {
        Post post = postRepository.findById(comment.getPost().getId()).get();
        if(post.getUser().getId() != comment.getUser().getId()){
            notificationRepository.save(Notification
                    .builder()
                    .to(post.getUser())
                    .from(comment.getUser())
                    .date(new Date())
                    .post(post)
                    .notificationStatus(NotificationStatus.NEW)
                    .notificationType(NotificationType.POST_COMMENT)
                    .build());
        }
        commentRepository.save(comment);
        LOGGER.debug("comment saved");
        return CommentDto
                .builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .sendDate(new SimpleDateFormat("dd.MM HH:mm:ss").format(comment.getSendDate()))
                .user(UserDto
                        .builder()
                        .id(comment.getUser().getId())
                        .name(comment.getUser().getName())
                        .surname(comment.getUser().getSurname())
                        .email(comment.getUser().getEmail())
                        .coverImg(comment.getUser().getCoverImg())
                        .profileImg(comment.getUser().getProfileImg())
                        .age(comment.getUser().getAge())
                        .city(comment.getUser().getCity())
                        .build())
                .build();
    }

    private void addChildrenList(List<CommentResponseDto> commentResponseDtos, int parentId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm:ss");
        List<Comment> comments = commentRepository.findAllByParentId(parentId);
        for (Comment comment : comments) {
            List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
            addChildrenList(commentResponseDtoList,comment.getId());
            commentResponseDtos.add(CommentResponseDto
                    .builder()
                    .commentDto(CommentDto
                            .builder()
                            .id(comment.getId())
                            .comment(comment.getComment())
                            .sendDate(dateFormat.format(comment.getSendDate()))
                            .user(UserDto
                                    .builder()
                                    .id(comment.getUser().getId())
                                    .name(comment.getUser().getName())
                                    .surname(comment.getUser().getSurname())
                                    .email(comment.getUser().getEmail())
                                    .coverImg(comment.getUser().getCoverImg())
                                    .profileImg(comment.getUser().getProfileImg())
                                    .age(comment.getUser().getAge())
                                    .city(comment.getUser().getCity())
                                    .build())
                            .build())
                    .childrens(commentResponseDtoList)
                    .build());
        }
    }


}
