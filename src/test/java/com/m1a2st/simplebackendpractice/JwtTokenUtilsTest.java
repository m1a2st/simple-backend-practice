package com.m1a2st.simplebackendpractice;

import com.m1a2st.simplebackendpractice.utils.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author m1a2st
 * @Date 2023/3/23
 * @Version v1.0
 */
@SpringBootTest
public class JwtTokenUtilsTest {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Test
    void decode_check(){
        String token = jwtTokenUtils.generateToken("m1a2st", UUID.randomUUID().toString());
        String encode = jwtTokenUtils.retrieveSubject(token);
        assertEquals("m1a2st", encode);
    }
}
