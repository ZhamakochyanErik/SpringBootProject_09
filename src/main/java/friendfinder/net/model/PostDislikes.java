package friendfinder.net.model;

import friendfinder.net.model.enums.LikesOrDislikesStatus;
import lombok.*;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDislikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private LikesOrDislikesStatus dislikeStatus;
}