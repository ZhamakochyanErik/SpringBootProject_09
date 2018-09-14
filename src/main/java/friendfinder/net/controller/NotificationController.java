package friendfinder.net.controller;

import friendfinder.net.config.security.CurrentUser;
import friendfinder.net.service.NotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class NotificationController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification")
    public @ResponseBody
    ResponseEntity notifications(@AuthenticationPrincipal CurrentUser user){
        return ResponseEntity
                .ok(notificationService.getTop10ByNewNotByUserId(user.getUser().getId()));
    }

    @PostMapping("/notification/status/update")
    public @ResponseBody
    ResponseEntity updateStatus(@RequestBody List<Integer> notificationList){
        LOGGER.debug("notifications : {}",notificationList);
        notificationService.updateStatus(notificationList);
        return ResponseEntity
                .ok()
                .build();
    }
}