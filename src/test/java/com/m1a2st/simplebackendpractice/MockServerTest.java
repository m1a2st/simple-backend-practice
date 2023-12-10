package com.m1a2st.simplebackendpractice;

import com.m1a2st.simplebackendpractice.user.dto.UserSignupReqDTO;
import com.m1a2st.simplebackendpractice.user.repository.UserProfileRepository;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import static java.lang.String.format;

/**
 * @Author m1a2st
 * @Date 2023/3/28
 * @Version v1.0
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockServerTest {

    private static final MockWebServer mockWebServer = new MockWebServer();
    private static final int mockWebServerPort = new Random(new Date().getTime()).nextInt(10000) + 50000;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserProfileRepository userProfileRepository;

    @LocalServerPort
    private Integer port = 0;

    @BeforeAll
    static void beforeAll() {
        try {
            mockWebServer.start(mockWebServerPort);
            mockWebServer.setDispatcher(new Dispatcher() {
                @Override
                public @NotNull MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                    if (recordedRequest.getPath().matches("/")) {
                        return new MockResponse().setBody("123.jpeg");
                    } else {
                        return new MockResponse().setResponseCode(404);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void afterAll() {
        try {
            mockWebServer.shutdown();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach()
    void beforeEach(){
        userProfileRepository.deleteAll();
    }

    @Test
    void restTemplate_signup_successful() {
        ResponseEntity<String> test = restTemplate.postForEntity(format("http://localhost:%d/api/v1.0/user:signup",port)
                , new HttpEntity<>(new UserSignupReqDTO("Test", "123", "123")), String.class);
        System.out.println(test);
    }
}
