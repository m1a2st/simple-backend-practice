package com.m1a2st.simplebackendpractice.user;

import com.m1a2st.simplebackendpractice.config.security.LoginRequestDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserProfileReqDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserProfileRespDTO;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import com.m1a2st.simplebackendpractice.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

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
    private final JwtTokenUtils jwtTokenUtils;
    @Value("user.not.found")
    private String UNKNOWN;

    public UserProfileController(@Valid UserProfileService userProfileService, JwtTokenUtils jwtTokenUtils) {
        this.userProfileService = userProfileService;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Operation(summary = "註冊用戶")
    @PostMapping("/user:signup")
    public UserProfileRespDTO userSignUp(@RequestBody @Valid UserProfileReqDTO userProfileRespDTO){
        return userProfileService.signup(userProfileRespDTO);
    }

    @Operation(summary = "使用者登入")
    @PostMapping("/user:login")
    public ResponseEntity<String> userLogin(@RequestBody LoginRequestDTO loginRequestDTO){
        UserProfile user = userProfileService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        if(UNKNOWN.equals(user.getUsername())){
            throw new RuntimeException("User not found");
        }
        String token = jwtTokenUtils.generateToken(user.getUsername(), UUID.randomUUID().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",token))
                .body(token);
    }

}
