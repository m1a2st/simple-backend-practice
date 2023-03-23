package com.m1a2st.simplebackendpractice.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.security.auth.message.AuthException;
import java.time.Instant;
import java.util.Date;

/**
 * @Author m1a2st
 * @Date 2023/3/21
 * @Version v1.0
 */
@Component
public class JwtTokenUtils {

    private static final long EXPIRATION_TIME = 600 * 1000;
    /**
     * JWT SECRET KEY
     */
    private static final String SECRET = "learn to dance in the rain";

    /**
     * 簽發JWT
     */
    public String generateToken(String subject, String uuid) {
        return Jwts.builder()
                .setIssuer("backend")
                .setSubject(subject)
                .setExpiration(new Date(Instant.now().toEpochMilli() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setId(uuid)
                .setNotBefore(new Date())
                .setIssuedAt(new Date())
                .compact();
    }
    /**
     * 驗證JWT
     */
    public String retrieveSubject(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
