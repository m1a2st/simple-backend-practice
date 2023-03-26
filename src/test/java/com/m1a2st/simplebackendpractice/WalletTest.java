package com.m1a2st.simplebackendpractice;

import com.m1a2st.simplebackendpractice.user.repository.UserProfileRepository;
import com.m1a2st.simplebackendpractice.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Objects;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author m1a2st
 * @Date 2023/3/24
 * @Version v1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
public class WalletTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    public void deleteData() {
        userProfileRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    void open_wallet_success() throws Exception {
        signupAndLoginDoSomething(get("/api/v1.0/wallet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").exists())
                .andExpect(jsonPath("username").value("WalletUser"))
                .andExpect(jsonPath("balance").value(0));
    }

    private ResultActions signupAndLoginDoSomething(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception {
        mockMvc.perform(post("/api/v1.0/user:signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(format("{\"username\": \"%s\", \"password\": \"%s\", \"repeatPassword\": \"%s\"}", "WalletUser", "123", "123")));
        mockMvc.perform(post("/api/v1.0/user:login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(format("{\"username\":\"%s\",\"password\":\"%s\"}", "WalletUser", "123")));
        MvcResult result = mockMvc.perform(post("/api/v1.0/user:login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(format("{\"username\":\"%s\",\"password\":\"%s\"}", "WalletUser", "123")))
                .andExpect(status().isOk())
                .andReturn();
        String token = Objects.requireNonNull(result.getResponse().getHeader(HttpHeaders.AUTHORIZATION));
        return mockMvc.perform(mockHttpServletRequestBuilder.header(HttpHeaders.AUTHORIZATION, token));
    }
}
