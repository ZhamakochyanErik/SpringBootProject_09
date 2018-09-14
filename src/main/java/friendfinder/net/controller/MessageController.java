package friendfinder.net.controller;

import friendfinder.net.config.security.CurrentUser;
import friendfinder.net.service.MessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private MessageService messageService;

    @GetMapping("/message")
    public @ResponseBody
    ResponseEntity messages(@AuthenticationPrincipal CurrentUser user){
        return ResponseEntity
                .ok(messageService.getTop10NewMessagesByToId(user.getUser().getId()));
    }
}