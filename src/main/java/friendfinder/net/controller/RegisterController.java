package friendfinder.net.controller;

import friendfinder.net.form.MailForm;
import friendfinder.net.form.UserRegisterForm;
import friendfinder.net.model.City;
import friendfinder.net.model.User;
import friendfinder.net.model.enums.ActivationType;
import friendfinder.net.model.enums.UserRole;
import friendfinder.net.repository.CityRepository;
import friendfinder.net.repository.UserRepository;
import friendfinder.net.service.MailService;
import friendfinder.net.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Controller
public class RegisterController implements Pages{

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

    @GetMapping("/register")
    public String registerGet(Model model){
        model.addAttribute("userForm",new UserRegisterForm());
        return REGISTER;
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute("userForm") @Valid UserRegisterForm userForm,
                               BindingResult result,Locale locale){
        LOGGER.debug("form : {}",userForm);
        int age;
        if(result.hasErrors()){
            return REGISTER;
        }else if(userService.existsByEmail(userForm.getEmail())){
            result.addError(new FieldError("userForm","email",""));
            return REGISTER;
        }else if(!userForm.getPassword().equals(userForm.getRePassword())){
            result.addError(new FieldError("userForm","password",""));
            result.addError(new FieldError("userForm","rePassword",""));
            return REGISTER;
        }else if((age = getAge(userForm.getBirthDate())) < 14 || age > 100){
            result.addError(new FieldError("userForm","birthDate",""));
            return REGISTER;
        }else {
            String token = UUID.randomUUID().toString();
            MailForm mailForm = getMailForm(userForm.getEmail(),token,locale);
            User user = User
                    .builder()
                    .name(userForm.getName())
                    .surname(userForm.getSurname())
                    .email(userForm.getEmail())
                    .password(userForm.getPassword())
                    .age(age)
                    .birthDate(userForm.getBirthDate())
                    .role(UserRole.USER)
                    .token(token)
                    .activationType(ActivationType.INACTIVE)
                    .city(City.builder()
                            .id(userForm.getCityId())
                            .build())
                    .build();
            userService.add(user);
            mailService.sendMessage(mailForm);
            return "redirect:/login?email-message-send";
        }
    }

    @GetMapping("/email-confirm/{token}/{email}")
    public String confirmUserEmail(@PathVariable("token")String token,
                                   @PathVariable("email")String email){
        Optional<User> optionalUser = userService.getByEmail(email);
        if(optionalUser.isPresent() && optionalUser.get().getToken().equals(token)){
            User user = optionalUser.get();
            LOGGER.debug("user : {} \n confirmed email address",user);
            user.setToken("");
            user.setActivationType(ActivationType.ACTIVE);
            userService.update(user);
            return "redirect:/login?email-confirmed";
        }else {
            return "redirect:/404";
        }
    }

    private MailForm getMailForm(String email,String token, Locale locale){
        String subject;
        if(locale.getLanguage().equals("ru")){
            subject = subjectRu;
        }else if(locale.getLanguage().equals("en")){
            subject = subjectEn;
        }else {
            subject = subjectArm;
        }
        return MailForm.builder()
                .to(email)
                .subject(subject)
                .text("http://localhost:8080/email-confirm/" + token + "/" + email)
                .build();
    }

    static int getAge(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] dateArray = dateFormat.format(date).split("-");
        String[] currentDateArray = dateFormat.format(new Date()).split("-");
        int month = dateArray[1].startsWith("0") ? Integer.parseInt(dateArray[1].substring(1)) : Integer.parseInt(dateArray[1]);
        int day = dateArray[2].startsWith("0") ? Integer.parseInt(dateArray[2].substring(1)) : Integer.parseInt(dateArray[2]);
        int currentMonth = currentDateArray[1].startsWith("0") ? Integer.parseInt(currentDateArray[1].substring(1)) : Integer.parseInt(currentDateArray[1]);
        int currentDay = currentDateArray[2].startsWith("0") ? Integer.parseInt(currentDateArray[2].substring(1)) : Integer.parseInt(currentDateArray[2]);;
        int size = 0;
        if(currentMonth - month < 0){
            size = 1;
        }else if(currentMonth - month == 0){
            if(currentDay - day < 0){
                size = 1;
            }
        }
        return (Integer.parseInt(currentDateArray[0]) - Integer.parseInt(dateArray[0])) - size;
    }
}