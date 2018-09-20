package friendfinder.net.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestForm {

    private int id;

    private String title;

    private String description;

    private MultipartFile image;
}