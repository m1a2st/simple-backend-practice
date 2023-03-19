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
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author m1a2st
 * @Date 2023/3/18
 * @Version v1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserProfileTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserProfileRepository repository;

    @Test
    void defaultTest(){

    }

    @BeforeEach
    void beforeEach(){
        repository.deleteAll();
    }

    @Test
    void insertUser(){
        UserProfile ken = UserProfile.builder()
                .username("Ken")
                .password("123")
                .status(UserStatus.ACTIVE)
                .role(UserRole.ADMIN)
                .build();
        repository.save(ken);
        UserProfile result = repository.findByUsername("Ken").orElseGet(UserProfile::new);
        assertEquals(result.getUsername(),"Ken");
    }

}
