package friendfinder.net.controller;

import friendfinder.net.config.security.CurrentUser;
import friendfinder.net.dto.MessageRequestDto;
import friendfinder.net.model.Message;
import friendfinder.net.model.User;
import friendfinder.net.model.enums.MessageStatus;
import friendfinder.net.service.MessageService;
import friendfinder.net.service.UserService;
import friendfinder.net.util.ImageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@Controller
public class MessageController implements Pages{

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageUtil imageUtil;

    @GetMapping("/message")
    public @ResponseBody
    ResponseEntity messages(@AuthenticationPrincipal CurrentUser user){
        return ResponseEntity
                .ok(messageService.getTop10NewMessagesByToId(user.getUser().getId()));
    }

    @GetMapping("/user/messages")
    public String userMessages(Model model, @RequestAttribute User user){
        model.addAttribute("messageData",messageService.getMessageData(user,null));
        return USER_MESSAGES;
    }

    @GetMapping("/user/messages/to/{id}")
    public String messageByUserId(@PathVariable("id")String strId,
                                  @RequestAttribute User user,
                                  Model model){
        LOGGER.debug("userId : {}",strId);
        try {
            Optional<User> optionalUser = userService.getById(Integer.parseInt(strId));
            if(!optionalUser.isPresent() || optionalUser.get().getId() == user.getId()){
                throw new NumberFormatException();
            }
            model.addAttribute("messageData",
                    messageService.getMessageData(user,optionalUser.get()));
        }catch (Exception e){
            return "redirect:/";
        }
        return USER_MESSAGES;
    }

    @PostMapping("/user/message/add")
    public @ResponseBody
    ResponseEntity add(MessageRequestDto messageRequestDto,
                       @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("messageDto : {}",messageRequestDto);
        boolean imageExists = false;
        Optional<User> optionalUser;
        if(messageRequestDto.getImage().isEmpty() && (messageRequestDto.getMessage() == null ||
                messageRequestDto.getMessage().equals(""))){
            return ResponseEntity
                    .badRequest()
                    .build();
        }else if(!(optionalUser = userService.getById(messageRequestDto.getUserId())).isPresent() ||
                optionalUser.get().getId() == currentUser.getUser().getId()){
            return ResponseEntity
                    .badRequest()
                    .build();
        }
        if (!messageRequestDto.getImage().isEmpty() && imageUtil.isValidFormat(messageRequestDto.getImage().getContentType())) {
            imageExists = true;
        }
        if(messageRequestDto.getMessage() != null && messageRequestDto.getMessage().equals("")){
            messageRequestDto.setMessage(null);
        }
        Message message = Message
                .builder()
                .from(currentUser.getUser())
                .to(optionalUser.get())
                .message(messageRequestDto.getMessage())
                .imgUrl(null)
                .messageStatus(MessageStatus.NEW)
                .sendDate(new Date())
                .build();
        return ResponseEntity
                .ok(messageService.add(message,messageRequestDto.getImage(),imageExists));
    }

    @GetMapping("/user/messages/to/{id}/load/new")
    public @ResponseBody
    ResponseEntity loadNewMessages(@PathVariable("id")int userId,
                                   @AuthenticationPrincipal CurrentUser currentUser){
        return ResponseEntity
                .ok(messageService.getAllNewMessages(currentUser.getUser(),userId));
    }
}