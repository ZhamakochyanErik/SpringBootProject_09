package friendfinder.net.form;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoverPasswordForm {

    private String email;

    private String password1;

    private String password2;
}