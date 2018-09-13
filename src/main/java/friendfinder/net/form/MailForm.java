package friendfinder.net.form;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailForm {

    private String to;

    private String subject;

    private String text;
}