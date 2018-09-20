package friendfinder.net.controller;

import friendfinder.net.form.ChangeEmailForm;
import friendfinder.net.form.ChangePasswordForm;
import friendfinder.net.form.MailForm;
import friendfinder.net.form.UserUpdateForm;
import friendfinder.net.model.City;
import friendfinder.net.model.User;
import friendfinder.net.service.MailService;
import friendfinder.net.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;
import java.util.UUID;

@Controller
@RequestMapping("/user/settings")
public class UserSettingsController implements Pages{

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;


    @Value("${email.subject.en}")
    private String subjectEn;

    @Value("${email.subject.ru}")
    private String subjectRu;

    @Value("${email.subject.arm}")
    private String subjectArm;

    @GetMapping({"","/update/data"})
    public String userSettings(Model model,
                               @RequestAttribute User user){
        model.addAttribute("userForm",buildUserForm(user));
        model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
        return CHANGE_DATA;
    }

    private UserUpdateForm buildUserForm(User user){
        return UserUpdateForm
                .builder()
                .name(user.getName())
                .surname(user.getSurname())
                .birthDate(user.getBirthDate())
                .cityId(user.getCity().getId())
                .build();
    }

    @PostMapping("/data/update")
    public String updatePost(@ModelAttribute("userForm") @Valid UserUpdateForm userForm,
                             BindingResult result, Model model, @RequestAttribute User user){
        LOGGER.debug("form : {}",userForm);
        int age;
        if(result.hasErrors()){
            model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
            return CHANGE_DATA;
        }else if(!passwordEncoder.matches(userForm.getPassword(),user.getPassword())){
            result.addError(new FieldError("userForm","password",""));
            model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
            return CHANGE_DATA;
        }else if((age = RegisterController.getAge(userForm.getBirthDate())) < 14 || age > 100){
            result.addError(new FieldError("userForm","birthDate",""));
            model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
            return CHANGE_DATA;
        }else {
            user.setName(userForm.getName());
            user.setSurname(userForm.getSurname());
            user.setPassword(userForm.getPassword());
            user.setAge(age);
            user.setBirthDate(userForm.getBirthDate());
            user.setCity(City
                    .builder()
                    .id(userForm.getCityId())
                    .build());
            userService.add(user);
            return "redirect:/user/settings?dataSuccess";
        }
    }

    @GetMapping("/change/email")
    public String changeEmailGet(Model model,
                                 @RequestAttribute User user){
        model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
        return CHANGE_EMAIL;
    }

    @PostMapping("/change/email")
    public String changeEmailPost(@RequestAttribute("user") User user, ChangeEmailForm form,
                                  Model model, Locale locale){
        if(form.getEmail() == null || (!form.getEmail().endsWith("@gmail.com") &&
                !form.getEmail().endsWith("@mail.ru") && !form.getEmail().endsWith("@mail.com")) ||
                userService.getByEmail(form.getEmail()).isPresent()){
            model.addAttribute("emailError","");
            model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
            return CHANGE_EMAIL;
        }else if(!passwordEncoder.matches(form.getPassword(),user.getPassword())){
            model.addAttribute("passwordError","");
            model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
            return CHANGE_EMAIL;
        }else if(!user.getToken().equals("") && user.getToken().split("/")[1].equals(form.getEmail())){
            return "redirect:/user/settings/change/email?error-email-already-exists";
        }else {
            String token = UUID.randomUUID().toString();
            user.setToken(token + "/" + form.getEmail());
            String subject;
            switch (locale.getLanguage()){
                case "en":
                    subject = subjectEn;
                    break;
                case "ru":
                    subject = subjectRu;
                    break;
                default:
                    subject = subjectArm;
            }
            userService.update(user);
            mailService.sendMessage(MailForm
                    .builder()
                    .to(form.getEmail())
                    .text("http://localhost:8080/user/settings/confirmed-new-email/" +
                            form.getEmail() + "/" + token)
                    .subject(subject)
                    .build());
            return "redirect:/user/settings/change/email?success-send-message";
        }
    }

    @GetMapping("/confirmed-new-email/{email}/{token}")
    public String confirmedNewEmail(@PathVariable("email")String email,
                                    @PathVariable("token")String token,
                                    @RequestAttribute("user")User user){
        String persistToken = user.getToken().split("/")[0];
        String persistEmail = user.getToken().split("/")[1];
        if(persistToken.equals(token) && persistEmail.equals(email)){
            user.setToken("");
            user.setEmail(email);
            userService.update(user);
            return "redirect:/user/settings/change/email?success-email-confirmed";
        }else {
            return "redirect:/404";
        }
    }

    @GetMapping("/change/password")
    public String changePasswordGet(Model model, @RequestAttribute User user){
        model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
        model.addAttribute("passwordForm",new ChangePasswordForm());
        return CHANGE_PASSWORD;
    }

    @PostMapping("/change/password")
    public String changePasswordPost(@ModelAttribute("passwordForm") @Valid ChangePasswordForm form,
                                     BindingResult result,@RequestAttribute User user,Model model){
        LOGGER.debug("passwordForm : {}",form);
        if(result.hasErrors()){
            model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
            return CHANGE_PASSWORD;
        }else if(!passwordEncoder.matches(form.getPersistPassword(),user.getPassword())){
            result.addError(new FieldError("passwordForm","persistPassword",""));
            model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
            return CHANGE_PASSWORD;
        }else if(!form.getPassword().equals(form.getRePassword())){
            result.addError(new FieldError("passwordForm","rePasswordDoNotMatch",""));
            model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
            return CHANGE_PASSWORD;
        }else {
            user.setPassword(passwordEncoder.encode(form.getPassword()));
            userService.update(user);
            return "redirect:/user/settings/change/password?success";
        }
    }
}
