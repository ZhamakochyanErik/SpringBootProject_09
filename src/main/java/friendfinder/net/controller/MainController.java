package friendfinder.net.controller;

import friendfinder.net.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
public class MainController implements Pages{

    @GetMapping("/")
    public String main(@RequestAttribute("user")User user, Model model){
        return INDEX;
    }
}