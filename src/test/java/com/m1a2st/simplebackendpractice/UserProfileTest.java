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
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void defaultTest() {

    }

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }

    @Test
    void insertUser() {
        UserProfile ken = UserProfile.builder()
                .username("Ken")
                .password("123")
                .status(UserStatus.ACTIVE)
                .role(UserRole.ADMIN)
                .build();
        repository.save(ken);
        UserProfile result = repository.findByUsername("Ken").orElseGet(UserProfile::new);
        assertEquals(result.getUsername(), "Ken");
    }

    @Test
    void signUp_successful() throws Exception {
        mockMvc.perform(post("/api/v1.0/user:signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "Ken")
                        .param("password", "123")
                        .param("repeatPassword", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("username").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("password").doesNotExist());
    }

    

}
