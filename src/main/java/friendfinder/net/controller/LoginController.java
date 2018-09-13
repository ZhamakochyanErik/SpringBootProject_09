package friendfinder.net.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController implements Pages{

    @GetMapping("/login")
    public String login(){
        return LOGIN;
    }
}