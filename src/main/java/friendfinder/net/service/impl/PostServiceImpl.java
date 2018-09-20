package friendfinder.net.service.impl;

import friendfinder.net.data.PostData;
import friendfinder.net.dto.IntegerDto;
import friendfinder.net.model.*;
import friendfinder.net.model.enums.LikesOrDislikesStatus;
import friendfinder.net.model.enums.NotificationStatus;
import friendfinder.net.model.enums.NotificationType;
import friendfinder.net.model.enums.RequestStatus;
import friendfinder.net.repository.*;
import friendfinder.net.service.PostService;
import friendfinder.net.util.ImageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private PostRepository postRepository;


    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private PostLikesRepository postLikesRepository;

    @Autowired
    private PostDislikeRepository postDislikeRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ImageUtil imageUtil;

    @Override
    public List<PostData> getAllByUserId(User user, Pageable pageable) {
        List<User> users = new ArrayList<>();
        List<PostData> postDataList = new ArrayList<>();
        users.add(user);
        for (FriendRequest friendRequest : friendRequestRepository
                .findAllByFromIdOrToIdAndRequestStatus(user.getId(),RequestStatus.ACCEPTED)) {
            if(friendRequest.getFrom().getId() != user.getId()){
                users.add(friendRequest.getFrom());
            }else {
                users.add(friendRequest.getTo());
            }
        }
        List<Post> posts = postRepository.findAllByUserIn(users, pageable);
        for (Post post : posts) {
            postDataList.add(PostData
                    .builder()
                    .post(post)
                    .commentsCount(commentRepository.countByPostId(post.getId()))
                    .likesCount(postLikesRepository.countByPostId(post.getId()))
                    .dislikesCount(postDislikeRepository.countByPostId(post.getId()))
                    .liked(postLikesRepository.existsByUserIdAndPostId(user.getId(),post.getId()))
                    .disliked(postDislikeRepository.existsByUserIdAndPostId(user.getId(),post.getId()))
                    .build());
        }
        return postDataList;
    }

    @Override
    public List<PostData> getAllUserPosts(int userId,int currentUserId, Pageable pageable) {
        List<PostData> postDataList = new ArrayList<>();
        List<Post> posts = postRepository.findAllByUserId(userId, pageable);
        for (Post post : posts) {
            postDataList.add(PostData
                    .builder()
                    .post(post)
                    .commentsCount(commentRepository.countByPostId(post.getId()))
                    .likesCount(postLikesRepository.countByPostId(post.getId()))
                    .dislikesCount(postDislikeRepository.countByPostId(post.getId()))
                    .liked(postLikesRepository.existsByUserIdAndPostId(currentUserId,post.getId()))
                    .disliked(postDislikeRepository.existsByUserIdAndPostId(currentUserId,post.getId()))
                    .build());
        }
        return postDataList;
    }

    @Transactional
    public IntegerDto addLike(int postId, int userId) {
        int statusCode;
        PostLikes like = postLikesRepository.findByPostIdAndUserId(postId, userId);
        if(like == null){
            PostDislikes dislike = postDislikeRepository.findByPostIdAndUserId(postId, userId);
            if(dislike != null){
                postDislikeRepository.delete(dislike);
                statusCode = 201;
            }else {
                statusCode = 200;
            }
            if(!postRepository.existsByUserIdAndId(userId,postId)) {
                notificationRepository.save(Notification
                        .builder()
                        .to(postRepository.findById(postId).get().getUser())
                        .from(User.builder().id(userId).build())
                        .post(Post.builder().id(postId).build())
                        .notificationType(NotificationType.POST_LIKE)
                        .notificationStatus(NotificationStatus.NEW)
                        .date(new Date())
                        .build());
            }
            postLikesRepository.save(PostLikes
                    .builder()
                    .likeStatus(LikesOrDislikesStatus.NEW)
                    .post(Post.builder().id(postId).build())
                    .user(User.builder().id(userId).build())
                    .build());
        }else {
            postLikesRepository.delete(like);
            statusCode = 199;
        }
        return new IntegerDto(statusCode);
    }

    @Transactional
    public IntegerDto addDislike(int postId, int userId) {
        int statusCode;
        PostDislikes dislike = postDislikeRepository.findByPostIdAndUserId(postId, userId);
        if(dislike == null){
            PostLikes like = postLikesRepository.findByPostIdAndUserId(postId, userId);
            if(like != null){
                postLikesRepository.delete(like);
                statusCode = 201;
            }else {
                statusCode = 200;
            }
            if(!postRepository.existsByUserIdAndId(userId,postId)) {
                notificationRepository.save(Notification
                        .builder()
                        .from(User.builder().id(userId).build())
                        .to(postRepository.findById(postId).get().getUser())
                        .post(Post.builder().id(postId).build())
                        .notificationType(NotificationType.POST_DISLIKE)
                        .notificationStatus(NotificationStatus.NEW)
                        .date(new Date())
                        .build());
            }
            postDislikeRepository.save(PostDislikes
                    .builder()
                    .dislikeStatus(LikesOrDislikesStatus.NEW)
                    .post(Post.builder().id(postId).build())
                    .user(User.builder().id(userId).build())
                    .build());
        }else {
            postDislikeRepository.delete(dislike);
            statusCode = 199;
        }
        return new IntegerDto(statusCode);

    }

    @Transactional
    public void deletePost(int userId, int postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if(optionalPost.isPresent() && optionalPost.get().getUser().getId() == userId){
            postRepository.delete(optionalPost.get());
            imageUtil.delete("posts\\" + postId);
            LOGGER.debug("post deleted");
        }else {
            LOGGER.debug("invalid post");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(Post post, MultipartFile image, boolean existsImage) {
        postRepository.save(post);
        if(existsImage){
            String imgUrl = System.currentTimeMillis() + image.getOriginalFilename();
            try {
                imageUtil.save("posts\\" + post.getId(),imgUrl,image);
                post.setImgUrl(post.getId() + "/" + imgUrl);
                postRepository.flush();
            }catch (Exception e){
                imageUtil.delete("posts\\" + post.getId());
                throw new RuntimeException(e);
            }
        }
        LOGGER.debug("post saved");
    }

    @Override
    public PostData getPageDataById(int id, User user) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if(!optionalPost.isPresent()){
            throw new NumberFormatException();
        }
        Post post = optionalPost.get();
        return PostData
                .builder()
                .post(post)
                .commentsCount(commentRepository.countByPostId(post.getId()))
                .likesCount(postLikesRepository.countByPostId(post.getId()))
                .dislikesCount(postDislikeRepository.countByPostId(post.getId()))
                .liked(postLikesRepository.existsByUserIdAndPostId(user.getId(),post.getId()))
                .disliked(postDislikeRepository.existsByUserIdAndPostId(user.getId(),post.getId()))
                .build();
    }

    @Override
    public Optional<Post> getById(int id) {
        return postRepository.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Post post, MultipartFile image, boolean existsImage) {
        if(existsImage){
            imageUtil.save("posts\\" + post.getId(),post.getImgUrl(),image);
            post.setImgUrl(post.getId() + "/" + post.getImgUrl());
        }else {
            if(post.getImgUrl() != null){
                imageUtil.delete("posts\\" + post.getId());
                post.setImgUrl(null);
            }
        }
        postRepository.save(post);
        LOGGER.debug("post updated");
    }
}
