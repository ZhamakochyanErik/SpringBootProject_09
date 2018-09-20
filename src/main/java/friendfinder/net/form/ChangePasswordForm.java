package friendfinder.net.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordForm {

    @Length(min = 4,max = 255)
    private String persistPassword;

    @Length(min = 4,max = 255)
    private String password;

    @Length(min = 4,max = 255)
    private String rePassword;

    private String rePasswordDoNotMatch;
}