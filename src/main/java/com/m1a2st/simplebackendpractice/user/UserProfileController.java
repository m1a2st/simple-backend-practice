package com.m1a2st.simplebackendpractice.user;

import com.m1a2st.simplebackendpractice.user.dto.UserProfileReqDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserProfileRespDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author m1a2st
 * @Date 2023/3/19
 * @Version v1.0
 */
@RequestMapping("/api/v1.0")
@RestController
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("/user:signUp")
    public UserProfileRespDTO userSignUp(UserProfileReqDTO userProfileRespDTO){
        return userProfileService.signUp(userProfileRespDTO);
    }
}
