package com.m1a2st.simplebackendpractice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.m1a2st.simplebackendpractice.config.security.LoginRequestDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserSignupReqDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserSignupRespDTO;
import com.m1a2st.simplebackendpractice.user.repository.UserProfileRepository;
import com.m1a2st.simplebackendpractice.wallet.dto.WalletRequestDTO;
import com.m1a2st.simplebackendpractice.wallet.dto.WalletTransferDTO;
import com.m1a2st.simplebackendpractice.wallet.repository.WalletRepository;
import com.m1a2st.simplebackendpractice.wallet.repository.WalletTransactionRecordRepository;
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

import java.math.BigDecimal;
import java.util.Objects;

import static java.lang.String.format;
import static org.hamcrest.Matchers.hasSize;
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

    @Autowired
    private WalletTransactionRecordRepository walletTransactionRecordRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void deleteData() {
        userProfileRepository.deleteAll();
        walletRepository.deleteAll();
        walletTransactionRecordRepository.deleteAll();
    }

    @Test
    void check_wallet_success() throws Exception {
        signupAndLoginDoSomething(get("/api/v1.0/wallet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").exists())
                .andExpect(jsonPath("username").value("WalletUser"))
                .andExpect(jsonPath("balance").value(0));
    }

    @Test
    void user_deposit() throws Exception {
        signupAndLoginDoSomething(post("/api/v1.0/wallet:deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(format("{\"amount\":\"%.0f\"}", new WalletRequestDTO(new BigDecimal(200)).getAmount())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("fromUserId").exists())
                .andExpect(jsonPath("beforeBalance").value(0))
                .andExpect(jsonPath("toUserId").exists())
                .andExpect(jsonPath("afterBalance").value(200));
    }

    @Test
    void user_withdraw_amount_fail() throws Exception {
        signupAndLoginDoSomething(post("/api/v1.0/wallet:withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(format("{\"amount\": \"%.0f\"}", new WalletRequestDTO(new BigDecimal(200)).getAmount())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void user_withdraw_account_success() throws Exception {
        // 註冊
        signup(new UserSignupReqDTO("WalletUser", "123", "123"));
        // 登入
        String token = login(new LoginRequestDTO("WalletUser", "123"));
        // 存款
        mockMvc.perform(post("/api/v1.0/wallet:deposit")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(format("{\"amount\":\"%.0f\"}", new WalletRequestDTO(new BigDecimal(200)).getAmount())))
                .andExpect(status().isOk());
        // 提款
        mockMvc.perform(post("/api/v1.0/wallet:withdraw")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(format("{\"amount\":\"%.0f\"}", new WalletRequestDTO(new BigDecimal(200)).getAmount())))
                .andExpect(status().isOk());
    }


    @Test
    void user_transfer_amount() throws Exception {
        Long testId = getSignupId(new UserSignupReqDTO("Test", "123", "123"));
        // 註冊
        signup(new UserSignupReqDTO("WalletUser", "123", "123"));
        // 登入
        String token = login(new LoginRequestDTO("WalletUser", "123"));
        // 存款
        mockMvc.perform(post("/api/v1.0/wallet:deposit")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(format("{\"amount\":\"%.0f\"}", new WalletRequestDTO(new BigDecimal(200)).getAmount())))
                .andExpect(status().isOk());
        // 匯款
        WalletTransferDTO walletTransferDTO = new WalletTransferDTO(testId, new BigDecimal(200));
        mockMvc.perform(post("/api/v1.0/wallet:transfer")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(format("{\"toUserId\":\"%s\",\"amount\":\"%.0f\"}", walletTransferDTO.getToUserId(), walletTransferDTO.getAmount())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("fromUserId").exists())
                .andExpect(jsonPath("beforeBalance").value(200))
                .andExpect(jsonPath("toUserId").value(testId))
                .andExpect(jsonPath("afterBalance").value(0));
        // 被匯款使用者登入
        String testToken = login(new LoginRequestDTO("Test", "123"));
        // 查看存款
        mockMvc.perform(get("/api/v1.0/wallet")
                        .header(HttpHeaders.AUTHORIZATION, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").exists())
                .andExpect(jsonPath("username").value("Test"))
                .andExpect(jsonPath("balance").value(200));
    }

    @Test
    void user_get_walletRecord() throws Exception {
        signupAndLoginDoSomething(get("/api/v1.0/wallet/record"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(0)));
    }

    private Long getSignupId(UserSignupReqDTO userSignupReqDTO) throws Exception {
        MvcResult testUser = signup(userSignupReqDTO);
        String resBody = testUser.getResponse().getContentAsString();
        UserSignupRespDTO userSignupRespDTO = objectMapper.readValue(resBody, UserSignupRespDTO.class);
        return userSignupRespDTO.getId();
    }

    private MvcResult signup(UserSignupReqDTO userSignupReqDTO) throws Exception {
        return mockMvc.perform(post("/api/v1.0/user:signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(format("{\"username\": \"%s\", \"password\": \"%s\", \"repeatPassword\": \"%s\"}",
                        userSignupReqDTO.getUsername(), userSignupReqDTO.getPassword(), userSignupReqDTO.getRepeatPassword()))).andReturn();
    }

    private String login(LoginRequestDTO loginRequestDTO) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1.0/user:login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(format("{\"username\":\"%s\",\"password\":\"%s\"}", loginRequestDTO.getUsername(), loginRequestDTO.getPassword())))
                .andExpect(status().isOk())
                .andReturn();
        return Objects.requireNonNull(result.getResponse().getHeader(HttpHeaders.AUTHORIZATION));
    }

    private ResultActions signupAndLoginDoSomething(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception {
        signup(new UserSignupReqDTO("WalletUser", "123", "123"));
        String token = login(new LoginRequestDTO("WalletUser", "123"));
        return mockMvc.perform(mockHttpServletRequestBuilder.header(HttpHeaders.AUTHORIZATION, token));
    }
}
