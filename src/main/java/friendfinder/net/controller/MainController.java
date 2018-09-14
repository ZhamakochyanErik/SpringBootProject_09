package friendfinder.net.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private static final Logger LOGGER = LogManager.getLogger();

    @GetMapping("/")
    public String main(){
        return "index";
    }
}