package com.example.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class TokenUtils {
    public static String createToken(String userId, String role, String sign) {
        return JWT.create()
                .withAudience(userId)          // 存入用户ID
                .withClaim("role", role)      // 存入角色标识
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h过期
                .sign(Algorithm.HMAC256(sign)); // 以用户密码作为密钥
    }
}
