package friendfinder.net.service;

import friendfinder.net.dto.CommentDto;
import friendfinder.net.dto.CommentResponseDto;
import friendfinder.net.model.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    List<CommentResponseDto> getComments(int postId, Pageable pageable);

    CommentDto add(Comment comment);
}