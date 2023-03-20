package com.m1a2st.simplebackendpractice.user;

import com.m1a2st.simplebackendpractice.user.dto.UserProfileReqDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserProfileRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author m1a2st
 * @Date 2023/3/19
 * @Version v1.0
 */
@Tag(name = "UserProfile")
@RequestMapping("/api/v1.0")
@RestController
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(@Valid UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Operation(summary = "註冊用戶")
    @PostMapping("/user:signup")
    public UserProfileRespDTO userSignUp(@Valid UserProfileReqDTO userProfileRespDTO){
        return userProfileService.signup(userProfileRespDTO);
    }
}
