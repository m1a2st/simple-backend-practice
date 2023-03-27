package com.m1a2st.simplebackendpractice;

import com.m1a2st.simplebackendpractice.config.security.LoginRequestDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserModifyPasswordDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserSignupReqDTO;
import com.m1a2st.simplebackendpractice.user.enu.UserRole;
import com.m1a2st.simplebackendpractice.user.enu.UserStatus;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import com.m1a2st.simplebackendpractice.user.repository.UserLoginRepository;
import com.m1a2st.simplebackendpractice.user.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Objects;

import static java.lang.String.format;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author m1a2st
 * @Date 2023/3/18
 * @Version v1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserProfileTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserLoginRepository userLoginRepository;


    @BeforeEach
    void deleteBeforeEach() {
        userProfileRepository.deleteAll();
        userLoginRepository.deleteAll();
    }

    @Test
    void find_user() throws Exception {
        signup(new UserSignupReqDTO("Ken", "123", "123")).andExpect(status().isOk());
        UserProfile userProfile = userProfileRepository.findByUsername("Ken").orElse(new UserProfile());
        assertEquals("Ken", userProfile.getUsername());
        assertEquals(UserStatus.ACTIVE, userProfile.getStatus());
    }

    @Test
    void test_server_error() throws Exception {
        signupAndLoginAndModifyPassword(post("/api/v1.0/user:modifyPassword"),
                new UserModifyPasswordDTO("123", "223"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void insertUser() {
        UserProfile ken = UserProfile.builder()
                .username("Ken")
                .password(passwordEncoder.encode("123"))
                .status(UserStatus.ACTIVE)
                .role(UserRole.ROLE_ADMIN)
                .build();
        userProfileRepository.save(ken);
        UserProfile result = userProfileRepository.findByUsername("Ken").orElseGet(UserProfile::new);
        assertEquals(result.getUsername(), "Ken");
    }

    @Test
    void signup_successful() throws Exception {
        signup(new UserSignupReqDTO("m1a2st", "123", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("username").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("password").doesNotExist());
    }

    @Test
    void signup_data_isNull() throws Exception {
        mockMvc.perform(post("/api/v1.0/user:signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signup_password_is_not_same() throws Exception {
        signup(new UserSignupReqDTO("m1a2st", "123", "223"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signup_cannotDuplicateUsername() throws Exception {
        signup(new UserSignupReqDTO("m1a2st", "123", "123"))
                .andExpect(status().isOk());
        signup(new UserSignupReqDTO("m1a2st", "223", "223"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_success() throws Exception {
        signup(new UserSignupReqDTO("Test", "123", "123"));
        login(new LoginRequestDTO("Test", "123"))
                .andExpect(status().isOk());
    }

    @Test
    void login_fail() throws Exception {
        signup(new UserSignupReqDTO("Test", "123", "123"));
        login(new LoginRequestDTO("Test", "223"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void user_getProfile() throws Exception {
        signupAndLoginDoSomething(get("/api/v1.0/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("$.username").value("Test"))
                .andExpect(jsonPath("password").doesNotExist())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("createDate").exists());
    }

    @Test
    void user_modify_password_success() throws Exception {
        signupAndLoginAndModifyPassword(patch("/api/v1.0/user:modifyPassword"),
                new UserModifyPasswordDTO("123", "223"))
                .andExpect(status().isNoContent());
    }

    @Test
    void user_modify_password_enter_wrong_oldPassword() throws Exception {
        signupAndLoginAndModifyPassword(patch("/api/v1.0/user:modifyPassword"),
                new UserModifyPasswordDTO("223", "323"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void user_modify_password_enter_same_password() throws Exception {
        signupAndLoginAndModifyPassword(patch("/api/v1.0/user:modifyPassword"),
                new UserModifyPasswordDTO("223", "223"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void user_stop_account_success() throws Exception {
        signupAndLoginDoSomething(patch("/api/v1.0//user/{username}:hibernate","Test"))
                .andExpect(status().isNoContent());
    }

    @Test
    void user_stop_account_fail() throws Exception {
        signupAndLoginDoSomething(patch("/api/v1.0//user/{username}:hibernate","m1a2st"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void user_get_records() throws Exception{
        signupAndLoginDoSomething(get("/api/v1.0/user/records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)));
    }

    private ResultActions login(LoginRequestDTO loginRequestDTO) throws Exception {
        return mockMvc.perform(post("/api/v1.0/user:login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(format("{\"username\":\"%s\",\"password\":\"%s\"}", loginRequestDTO.getUsername(), loginRequestDTO.getPassword())));
    }

    private ResultActions signup(UserSignupReqDTO userSignupReqDTO) throws Exception {
        return mockMvc.perform(post("/api/v1.0/user:signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(format("{\"username\": \"%s\", \"password\": \"%s\", \"repeatPassword\": \"%s\"}",
                        userSignupReqDTO.getUsername(), userSignupReqDTO.getPassword(), userSignupReqDTO.getRepeatPassword())));
    }

    private ResultActions signupAndLoginAndModifyPassword(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, UserModifyPasswordDTO userModifyPasswordDTO) throws Exception {
        return signupAndLoginDoSomething(mockHttpServletRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(format("{\"oldPassword\":\"%s\",\"newPassword\":\"%s\"}",
                        userModifyPasswordDTO.getOldPassword(), userModifyPasswordDTO.getNewPassword())));
    }

    private ResultActions signupAndLoginDoSomething(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception {
        signup(new UserSignupReqDTO("Test", "123", "123")).andExpect(status().isOk());
        MvcResult result = login(new LoginRequestDTO("Test", "123"))
                .andExpect(status().isOk())
                .andReturn();
        String token = Objects.requireNonNull(result.getResponse().getHeader(HttpHeaders.AUTHORIZATION));
        return mockMvc.perform(mockHttpServletRequestBuilder.header(HttpHeaders.AUTHORIZATION, token));
    }

}
