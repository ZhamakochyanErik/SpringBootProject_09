package friendfinder.net.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private int id;

    private String comment;

    private UserDto user;

    private String sendDate;
}
