package com.m1a2st.simplebackendpractice.user.controller;

import com.m1a2st.simplebackendpractice.config.security.LoginRequestDTO;
import com.m1a2st.simplebackendpractice.user.dto.*;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import com.m1a2st.simplebackendpractice.user.repository.UserProfileRepository;
import com.m1a2st.simplebackendpractice.user.service.impl.UserProfileService;
import com.m1a2st.simplebackendpractice.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

import static java.lang.String.format;

/**
 * @Author m1a2st
 * @Date 2023/3/19
 * @Version v1.0
 */
@Tag(name = "UserProfile")
@RequestMapping("/api/v1.0")
@RestController
/*
  具有安全需求，即需要進行身份驗證方可進行存取或操作。
  這個註解通常用於軟體開發中，特別是在設計和實現系統安全功能時，
  可以幫助開發人員和測試人員更好地理解程式碼的安全需求
  ，並加強對這些需求的測試和驗證。
 */
@SecurityRequirement(name = "Authentication")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserProfileRepository userProfileRepository;

    public UserProfileController(@Valid UserProfileService userProfileService, JwtTokenUtils jwtTokenUtils,
                                 UserProfileRepository userProfileRepository) {
        this.userProfileService = userProfileService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.userProfileRepository = userProfileRepository;
    }

    @Operation(summary = "註冊用戶")
    @PostMapping("/user:signup")
    public UserSignupRespDTO userSignUp(@RequestBody @Valid UserSignupReqDTO userProfileRespDTO) {
        return userProfileService.signup(userProfileRespDTO);
    }

    @Operation(summary = "使用者登入")
    @PostMapping("/user:login")
    public ResponseEntity<String> userLogin(@RequestBody LoginRequestDTO loginRequestDTO) {
        UserProfile user = userProfileService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        String token = jwtTokenUtils.generateToken(user.getUsername(), UUID.randomUUID().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", token))
                .body(token);
    }

    @PreAuthorize("hasAnyRole('ROLE_DEFAULT','ROLE_ADMIN')")
    @Operation(summary = "查詢使用者資料")
    @GetMapping("/user")
    public UserDetailDTO getUserProfile(Principal principal) {
        String username = principal.getName();
        return userProfileService.getUserDetail(username);
    }

    @PreAuthorize("hasRole('DEFAULT')")
    @Operation(summary = "使用者修改密碼")
    @PatchMapping("/user:modifyPassword")
    public ResponseEntity<Objects> modifyPassword(
            @RequestBody UserModifyPasswordDTO userModifyPasswordDTO, Principal principal) {
        String username = principal.getName();
        userProfileService.modifyPassword(username, userModifyPasswordDTO);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_DEFAULT','ROLE_ADMIN')")
    @Operation(summary = "使用者帳號停權")
    @PatchMapping("/user/{username}:hibernate")
    public ResponseEntity<Objects> modifyStatus(@PathVariable("username") String username) {
        userProfileService.stopAccount(username);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "登入記錄")
    @PreAuthorize("hasRole('ROLE_DEFAULT')")
    @GetMapping("/user/records")
    public Page<UserLoginResponseDTO> getUserRecord(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            Principal principal) {
        return userProfileService.queryUserRecord(principal.getName(), page, size);
    }

}
