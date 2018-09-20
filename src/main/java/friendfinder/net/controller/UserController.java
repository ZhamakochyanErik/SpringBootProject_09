package friendfinder.net.controller;

import friendfinder.net.config.security.CurrentUser;
import friendfinder.net.dto.UserIdDto;
import friendfinder.net.model.User;
import friendfinder.net.service.FriendRequestService;
import friendfinder.net.service.UserService;
import friendfinder.net.util.PaginationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController implements Pages{

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int SEARCH_SIZE = 9;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private PaginationUtil paginationUtil;

    @GetMapping("/profile")
    public String currentUserProfile(Model model, @RequestAttribute("user")User user){
        model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
        return USER_PROFILE;
    }

    @GetMapping("/{id}/profile")
    public String userProfile(@PathVariable("id")String strId,
                              @RequestAttribute User user,Model model){
        try {
            int userId;
            Optional<User> optionalUser = userService.getById((userId = Integer.parseInt(strId)));
            if(!optionalUser.isPresent()){
                throw new NumberFormatException();
            }else if(optionalUser.get().getId() == user.getId()){
                return "redirect:/user/profile";
            }else {
                model.addAttribute("profileData",userService.getUserProfileDataByUserId(userId));
                model.addAttribute("userProfile",optionalUser.get());
                model.addAttribute("requestData",friendRequestService.getRequestData(user.getId(),userId));
            }
        }catch (NumberFormatException e){
            return "redirect:/";
        }
        return USER_PROFILE;
    }


    @GetMapping("/{id}/friends")
    public String userFriends(@PathVariable("id")String strId,
                              @RequestAttribute("user") User currentUser,Model model,
                              Pageable pageable){
        try {
            int userId;
            Optional<User> optionalUser = userService.getById((userId = Integer.parseInt(strId)));
            if(!optionalUser.isPresent()){
                throw new NumberFormatException();
            }else if(optionalUser.get().getId() == currentUser.getId()){
                return "redirect:/user/friends";
            }else {
                int length = paginationUtil
                        .getPaginationLength(userService.countFriendsByUserId(userId),pageable.getPageSize());
                pageable = paginationUtil.checkPageable(pageable,length);
                model.addAttribute("requestData",friendRequestService.getRequestData(currentUser.getId(),userId));
                model.addAttribute("profileData",userService.getUserProfileDataByUserId(userId));
                model.addAttribute("userProfile",optionalUser.get());
                model.addAttribute("friends",userService.getFriendsByUserId(userId,
                        PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),Sort.Direction.DESC,"id")));
                model.addAttribute("length",length);
                model.addAttribute("pageNumber",pageable.getPageNumber());
            }
        }catch (NumberFormatException e){
            return "redirect:/";
        }
        return USER_FRIENDS;
    }

    @GetMapping("/friends")
    public String currentUserFriends(@RequestAttribute User user, Model model,
                              Pageable pageable){
        int length = paginationUtil
                .getPaginationLength(userService.countFriendsByUserId(user.getId()),pageable.getPageSize());
        pageable = paginationUtil.checkPageable(pageable,length);
        model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
        model.addAttribute("friends",userService.getFriendsByUserId(user.getId(),
                PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),Sort.Direction.DESC,"id")));
        model.addAttribute("length",length);
        model.addAttribute("pageNumber",pageable.getPageNumber());
        return USER_FRIENDS;
    }

    @GetMapping("/bookmark")
    public String bookmark(Model model,@RequestAttribute User user,
                           Pageable pageable){
        int length = paginationUtil
                .getPaginationLength(userService.countBookmarkByUserId(user.getId()),pageable.getPageSize());
        pageable = paginationUtil.checkPageable(pageable,length);
        model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
        model.addAttribute("friends",userService.getBookmarkByUserId(user.getId(),pageable));
        model.addAttribute("length",length);
        model.addAttribute("pageNumber",pageable.getPageNumber());
        return USER_BOOKMARK;
    }

    @PostMapping("/bookmark")
    public @ResponseBody
    ResponseEntity bookmark(@RequestBody UserIdDto userDto,
                            @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("userDto : {}",userDto);
        return ResponseEntity
                .ok(userService.addBookmark(currentUser.getUser(),userDto.getUserId()));
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "name",required = false,defaultValue = "!")String name,
                         Model model,Pageable pageable,@RequestAttribute User user){
        if(name == null || name.equals("") || name.equals("!")){
            model.addAttribute("users",new ArrayList<>());
            model.addAttribute("length",1);
            model.addAttribute("count",0);
        }else {
            int count = userService.searchCount(name,user.getId());
            int length = paginationUtil.getPaginationLength(count,SEARCH_SIZE);
            pageable = paginationUtil.checkPageable(PageRequest.of(pageable.getPageNumber(),SEARCH_SIZE),length);
            model.addAttribute("users",userService.searchResult(name,user.getId(),
                    PageRequest.of(pageable.getPageNumber(),SEARCH_SIZE,Sort.Direction.DESC,"id")));
            model.addAttribute("length",length);
            model.addAttribute("pageNumber",pageable.getPageNumber());
            model.addAttribute("count",count);
        }
        return SEARCH;
    }
}