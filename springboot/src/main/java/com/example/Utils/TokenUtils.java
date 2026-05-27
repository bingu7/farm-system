package com.example.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class TokenUtils {
    private static final String DEFAULT_SECRET = "farm-system-jwt-secret";

    private TokenUtils() {
    }

    public static String createToken(String userId, String role) {
        return JWT.create()
                .withAudience(userId)          // 存入用户ID
                .withClaim("role", role)      // 存入角色标识
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h过期
                .sign(Algorithm.HMAC256(getSecret()));
    }

    public static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(getSecret());
    }

    private static String getSecret() {
        String secret = System.getenv("JWT_SECRET");
        return (secret == null || secret.isBlank()) ? DEFAULT_SECRET : secret;
    }
}
