package friendfinder.net.controller;

import friendfinder.net.form.MailForm;
import friendfinder.net.form.RecoverPasswordForm;
import friendfinder.net.model.User;
import friendfinder.net.model.enums.ActivationType;
import friendfinder.net.service.MailService;
import friendfinder.net.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Controller
public class LoginController implements Pages{

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Value("${email.subject.en}")
    private String subjectEn;

    @Value("${email.subject.ru}")
    private String subjectRu;

    @Value("${email.subject.arm}")
    private String subjectArm;

    @GetMapping("/login")
    public String login(){
        return LOGIN;
    }

    @GetMapping("/forgot/password")
    public String forgotPasswordGet(){
        return FORGOT_PASSWORD;
    }

    @PostMapping("/forgot/password")
    public String forgotPasswordPost(String email, Locale locale){
        LOGGER.debug("email : {}",email);
        Optional<User> optionalUser;
        if(!(optionalUser = userService.getByEmail(email)).isPresent()){
            return "redirect:/forgot/password?error";
        }else if(optionalUser.get().getActivationType() == ActivationType.INACTIVE){
            return "redirect:/forgot/password?error-send-message";
        }else {
            String subject;
            if(locale.getLanguage().equals("en")){
                subject = subjectEn;
            }else if(locale.getLanguage().equals("ru")){
                subject = subjectRu;
            }else {
                subject = subjectArm;
            }
            String token = UUID.randomUUID().toString();
            User user = optionalUser.get();
            user.setToken(token);
            user.setActivationType(ActivationType.INACTIVE);
            userService.update(user);
            mailService.sendMessage(MailForm
                    .builder()
                    .to(email)
                    .subject(subject)
                    .text("http://localhost:8080/recover-password/" + token + "/" + email)
                    .build());
            return "redirect:/forgot/password?success";
        }
    }

    @GetMapping("/recover-password/{token}/{email}")
    public String recoverPasswordGet(@PathVariable("token")String token,
                                     @PathVariable("email")String email,Model model){
        Optional<User> optionalUser = userService.getByEmail(email);
        if(optionalUser.isPresent() && optionalUser.get().getToken().equals(token)){
            User user = optionalUser.get();
            user.setToken("");
            user.setActivationType(ActivationType.ACTIVE);
            model.addAttribute("email",email);
            userService.update(user);
            return RECOVER_PASSWORD;
        }else {
            return "redirect:/404";
        }
    }

    @PostMapping("/recover-password")
    public String recoverPasswordPost(RecoverPasswordForm recoverPasswordForm,Model model){
        LOGGER.debug("form : {}",recoverPasswordForm);
        Optional<User> optionalUser;
        if(recoverPasswordForm.getPassword1() == null || recoverPasswordForm.getPassword1().length() < 4 ||
                recoverPasswordForm.getPassword2() == null || recoverPasswordForm.getPassword2().length() < 4){
            model.addAttribute("email",recoverPasswordForm.getEmail());
            model.addAttribute("lengthError","lengthError");
            return RECOVER_PASSWORD;
        }else if(!recoverPasswordForm.getPassword1().equals(recoverPasswordForm.getPassword2())){
            model.addAttribute("email",recoverPasswordForm.getEmail());
            model.addAttribute("error","error");
            return RECOVER_PASSWORD;
        }if((optionalUser = userService.getByEmail(recoverPasswordForm.getEmail())).isPresent()){
            User user = optionalUser.get();
            user.setPassword(recoverPasswordForm.getPassword1());
            userService.add(user);
        }
        return "redirect:/login";
    }
}