package com.example.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenUtils {
    private static final int MIN_SECRET_LENGTH = 32;

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    public void validateSecret() {
        if (secret == null || secret.isBlank() || secret.length() < MIN_SECRET_LENGTH) {
            throw new IllegalStateException("jwt.secret must be configured and at least 32 characters long");
        }
    }

    public String createToken(String userId, String role) {
        return JWT.create()
                .withAudience(userId)          // 存入用户ID
                .withClaim("role", role)      // 存入角色标识
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h过期
                .sign(getAlgorithm());
    }

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }
}
