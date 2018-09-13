package friendfinder.net.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterForm {

    @Length(min = 2,max = 255)
    private String name;

    @Length(min = 2,max = 255)
    private String surname;

    @Length(min = 11,max = 255)
    private String email;

    @Length(min = 4,max = 255)
    private String password;

    @Length(min = 4,max = 255)
    private String rePassword;

    @Past
    private Date birthDate;

    @Range(min = 1)
    private int cityId;
}