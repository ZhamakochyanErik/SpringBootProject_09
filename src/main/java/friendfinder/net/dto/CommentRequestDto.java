package friendfinder.net.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {

    private int parentId;

    private int postId;

    private String comment;
}