package friendfinder.net.data;

import friendfinder.net.model.Post;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostData {

    private Post post;

    private int likesCount;

    private int dislikesCount;

    private int commentsCount;

    private boolean liked;

    private boolean disliked;
}