package friendfinder.net.controller;

import friendfinder.net.config.security.CurrentUser;
import friendfinder.net.dto.UserIdDto;
import friendfinder.net.model.FriendRequest;
import friendfinder.net.model.User;
import friendfinder.net.model.enums.RequestStatus;
import friendfinder.net.service.FriendRequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class FriendRequestController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping("/friend-request/add")
    public @ResponseBody
    ResponseEntity addFriendRequest(@AuthenticationPrincipal CurrentUser currentUser,
                                    @RequestBody UserIdDto userIdDto){
        LOGGER.debug("userI : {}",userIdDto.getUserId());
        if(friendRequestService.isExistsByToIdOrFromId(currentUser.getUser().getId(),userIdDto.getUserId())){
            return ResponseEntity
                    .badRequest()
                    .build();
        }
        friendRequestService.add(FriendRequest
                .builder()
                .from(currentUser.getUser())
                .to(User.builder()
                        .id(userIdDto.getUserId())
                        .build())
                .requestStatus(RequestStatus.NEW)
                .sendDate(new Date())
                .build());
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/friend-request/delete")
    public @ResponseBody
    ResponseEntity deleteFriendRequest(@RequestBody UserIdDto userDto,
                                       @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("userId : {}",userDto.getUserId());
        friendRequestService.delete(userDto.getUserId(),currentUser.getUser().getId());
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/friend-request/accepted")
    public @ResponseBody
    ResponseEntity acceptFriendRequest(@RequestBody UserIdDto userDto,
                                       @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("userId : {}",userDto.getUserId());
        friendRequestService.accept(userDto.getUserId(),currentUser.getUser().getId());
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/friend/add/{userId}")
    public String acceptFriendRequest(@PathVariable("userId") int userId,
                                       @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("userId : {}",userId);
        friendRequestService.accept(userId,currentUser.getUser().getId());
        return "redirect:/user/" + userId + "/profile";
    }

    @GetMapping("/friend/delete/{userId}")
    public String deleteFriendRequest(@PathVariable("userId") int userId,
                                       @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("userId : {}",userId);
        friendRequestService.delete(userId,currentUser.getUser().getId());
        return "redirect:/user/" + userId + "/profile";
    }
}