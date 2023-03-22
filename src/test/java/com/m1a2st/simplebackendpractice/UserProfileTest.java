package com.m1a2st.simplebackendpractice;

import com.m1a2st.simplebackendpractice.user.UserProfileRepository;
import com.m1a2st.simplebackendpractice.user.enu.UserRole;
import com.m1a2st.simplebackendpractice.user.enu.UserStatus;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author m1a2st
 * @Date 2023/3/18
 * @Version v1.0
 */
@SpringBootTest()
@AutoConfigureMockMvc
public class UserProfileTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserProfileRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void deleteBeforeEach() {
        repository.deleteAll();
    }

    @Test
    void find_user() throws Exception {
        signup("Ken").andExpect(status().isOk());
        UserProfile userProfile = repository.findByUsername("Ken").orElse(new UserProfile());
        assertEquals("Ken", userProfile.getUsername());
        assertEquals(UserStatus.ACTIVE, userProfile.getStatus());
    }

    @Test
    void insertUser() {
        UserProfile ken = UserProfile.builder()
                .username("Ken")
                .password(passwordEncoder.encode("123"))
                .status(UserStatus.ACTIVE)
                .role(UserRole.ROLE_ADMIN)
                .build();
        repository.save(ken);
        UserProfile result = repository.findByUsername("Ken").orElseGet(UserProfile::new);
        assertEquals(result.getUsername(), "Ken");
    }

    @Test
    void signup_successful() throws Exception {
        signup("Ken")
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
    void signup_cannotDuplicateUsername() throws Exception {
        signup("Ken")
                .andExpect(status().isOk());
        signup("Ken")
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_success() throws Exception {
        signup("Test");
        login("123")
                .andExpect(status().isOk());
    }

    @Test
    void login_fail() throws Exception {
        login("223")
                .andExpect(status().isBadRequest());
    }

    private ResultActions login(String password) throws Exception {
        return mockMvc.perform(post("/api/v1.0/user:login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(format("{\"username\":\"Test\",\"password\":\"%s\"}", password)));
    }

    private ResultActions signup(String username) throws Exception {
        return mockMvc.perform(post("/api/v1.0/user:signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(format("{\"username\": \"%s\", \"password\": \"123\", \"repeatPassword\": \"123\"}", username)));
    }

}
