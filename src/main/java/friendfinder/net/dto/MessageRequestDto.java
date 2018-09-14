package friendfinder.net.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequestDto {

    private int id;

    private UserDto from;

    private String message;

    private String imgUrl;

    private String sendDate;
}