package friendfinder.net.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    private int id;

    private String title;

    private String description;

    private String createdDate;

    private String imgUrl;
}