package friendfinder.net.controller;

import friendfinder.net.config.security.CurrentUser;
import friendfinder.net.dto.IntegerDto;
import friendfinder.net.dto.NotificationResponseDto;
import friendfinder.net.service.NotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity
                .ok(new IntegerDto(notificationService.updateStatus(notificationList)));
    }

    @GetMapping("/notifications/page/{page}")
    public @ResponseBody
    ResponseEntity loadNotifications(@PathVariable("page")int page,@AuthenticationPrincipal CurrentUser user){
        NotificationResponseDto notificationResponseDto = notificationService.getAllByUserId(user.getUser().getId(),
                PageRequest.of(page, 10));
        return ResponseEntity
                .ok(notificationResponseDto);
    }
}