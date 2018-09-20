package friendfinder.net.controller;

import friendfinder.net.config.security.CurrentUser;
import friendfinder.net.dto.ImageDto;
import friendfinder.net.dto.StringDto;
import friendfinder.net.model.User;
import friendfinder.net.service.FriendRequestService;
import friendfinder.net.service.ImageService;
import friendfinder.net.service.UserService;
import friendfinder.net.util.ImageUtil;
import friendfinder.net.util.PaginationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserImageController implements Pages{

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageUtil imageUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private PaginationUtil paginationUtil;

    @PostMapping("/images/profile/upload")
    public @ResponseBody
    ResponseEntity uploadProfileImage(MultipartFile image, @AuthenticationPrincipal CurrentUser currentUser){
        if(image.isEmpty() || !imageUtil.isValidFormat(image.getContentType())){
            return ResponseEntity
                    .badRequest()
                    .build();
        }else {
            imageService.addProfileImage(image,currentUser.getUser());
            return ResponseEntity
                    .ok(new StringDto(currentUser.getUser().getProfileImg()));
        }
    }

    @PostMapping("/images/cover/upload")
    public @ResponseBody
    ResponseEntity uploadCoverImage(MultipartFile image, @AuthenticationPrincipal CurrentUser currentUser){
        if(image.isEmpty() || !imageUtil.isValidFormat(image.getContentType())){
            return ResponseEntity
                    .badRequest()
                    .build();
        }else {
            imageService.addCoverImage(image,currentUser.getUser());
            return ResponseEntity
                    .ok(new StringDto(currentUser.getUser().getCoverImg()));
        }
    }

    @GetMapping("/images")
    public String currentUserImages(@RequestAttribute User user, Model model,
                                    Pageable pageable){
        int length = paginationUtil
                .getPaginationLength(imageService.countByUserId(user.getId()),pageable.getPageSize());
        pageable = paginationUtil.checkPageable(pageable,length);
        model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
        model.addAttribute("images",imageService.getAllByUserId(user.getId(),pageable));
        model.addAttribute("length",length);
        model.addAttribute("pageNumber",pageable.getPageNumber());
        return USER_IMAGES;
    }

    @GetMapping("/{id}/images")
    public String userImages(@PathVariable("id")String strId, Model model,
                             @RequestAttribute("user") User currentUser, Pageable pageable){
        LOGGER.debug("userId : {}",strId);
        User user;
        try {
            Optional<User> optionalUser = userService.getById(Integer.parseInt(strId));
            if(!optionalUser.isPresent()){
                throw new NumberFormatException();
            }else if(currentUser.getId() == optionalUser.get().getId()){
                return "redirect:/user/images";
            }else {
                user = optionalUser.get();
            }
        }catch (NumberFormatException e){
            return "redirect:/";
        }
        int length = paginationUtil
                .getPaginationLength(imageService.countByUserId(user.getId()),pageable.getPageSize());
        pageable = paginationUtil.checkPageable(pageable,length);
        model.addAttribute("profileData",userService.getUserProfileDataByUserId(user.getId()));
        model.addAttribute("userProfile",user);
        model.addAttribute("requestData",friendRequestService.getRequestData(currentUser.getId(),user.getId()));
        model.addAttribute("images",imageService.getAllByUserId(user.getId(),pageable));
        model.addAttribute("length",length);
        model.addAttribute("pageNumber",pageable.getPageNumber());
        return USER_IMAGES;
    }

    @PostMapping("/images/change/profile/image")
    public @ResponseBody
    ResponseEntity changeProfileImage(@RequestBody ImageDto imageDto,
                                      @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("imageId : {}",imageDto.getImgId());
        imageService.changeProfileImage(currentUser.getUser(),imageDto.getImgId());
        return ResponseEntity
                .ok(new StringDto(currentUser.getUser().getProfileImg()));
    }

    @PostMapping("/images/change/cover/image")
    public @ResponseBody
    ResponseEntity changeCoverImage(@RequestBody ImageDto imageDto,
                                      @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("imageId : {}",imageDto.getImgId());
        imageService.changeCoverImage(currentUser.getUser(),imageDto.getImgId());
        return ResponseEntity
                .ok(new StringDto(currentUser.getUser().getCoverImg()));
    }

    @GetMapping("/images/{id}/delete")
    public String delete(@PathVariable("id") String strId,
                         @AuthenticationPrincipal CurrentUser currentUser){
        LOGGER.debug("imageId : {}",strId);
        try {
            imageService.deleteById(Integer.parseInt(strId),currentUser.getUser());
        }catch (NumberFormatException e){}
        return "redirect:/user/images";
    }

    @PostMapping("/images/upload")
    public String uploadImage(MultipartFile image,
                              @AuthenticationPrincipal CurrentUser currentUser){
        if(!image.isEmpty() && imageUtil.isValidFormat(image.getContentType())){
            imageService.add(image,currentUser.getUser());
        }
        return "redirect:/user/images";
    }
}