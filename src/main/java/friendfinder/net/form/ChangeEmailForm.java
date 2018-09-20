package friendfinder.net.form;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeEmailForm {

    private String email;

    private String password;
}