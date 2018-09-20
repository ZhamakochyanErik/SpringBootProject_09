package friendfinder.net.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private CommentDto commentDto;

    private List<CommentResponseDto> childrens;
}
