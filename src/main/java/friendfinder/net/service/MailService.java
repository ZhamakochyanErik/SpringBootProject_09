package friendfinder.net.service;

import friendfinder.net.form.MailForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendMessage(MailForm mailForm){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailForm.getTo());
        message.setSubject(mailForm.getSubject());
        message.setText(mailForm.getText());
        mailSender.send(message);
    }
}