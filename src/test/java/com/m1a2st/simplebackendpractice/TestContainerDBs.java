package com.m1a2st.simplebackendpractice;

import com.m1a2st.simplebackendpractice.config.security.LoginRequestDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserSignupReqDTO;
import com.m1a2st.simplebackendpractice.user.enu.UserRole;
import com.m1a2st.simplebackendpractice.user.enu.UserStatus;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import com.m1a2st.simplebackendpractice.user.repository.UserLoginRepository;
import com.m1a2st.simplebackendpractice.user.repository.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static java.lang.String.format;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author m1a2st
 * @Date 2023/3/27
 * @Version v1.0
 */

@Slf4j
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class TestContainerDBs {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserLoginRepository userLoginRepository;

    @Container
    private static final PostgreSQLContainer<?> postgresqlContainer =
            new PostgreSQLContainer<>("postgres:15");

    @Container
    private static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:6");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry){
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @BeforeAll
    static void beforeAll(){
        postgresqlContainer.start();
        mongoDBContainer.start();
    }

    @AfterAll
    static void afterAll(){
        postgresqlContainer.close();
        mongoDBContainer.close();
    }

    @BeforeEach
    void deleteBeforeEach() {
        userProfileRepository.deleteAll();
        userLoginRepository.deleteAll();
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

    private ResultActions signupAndLoginDoSomething(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception {
        signup(new UserSignupReqDTO("Test", "123", "123")).andExpect(status().isOk());
        MvcResult result = login(new LoginRequestDTO("Test", "123"))
                .andExpect(status().isOk())
                .andReturn();
        String token = Objects.requireNonNull(result.getResponse().getHeader(HttpHeaders.AUTHORIZATION));
        return mockMvc.perform(mockHttpServletRequestBuilder.header(HttpHeaders.AUTHORIZATION, token));
    }

    private ResultActions signup(UserSignupReqDTO userSignupReqDTO) throws Exception {
        return mockMvc.perform(post("/api/v1.0/user:signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(format("{\"username\": \"%s\", \"password\": \"%s\", \"repeatPassword\": \"%s\"}",
                        userSignupReqDTO.getUsername(), userSignupReqDTO.getPassword(), userSignupReqDTO.getRepeatPassword())));
    }
}


