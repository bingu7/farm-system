package com.example.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.entity.Account;
import com.example.exception.CustomException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        if (ObjectUtil.isEmpty(token)) {
            throw new CustomException("401", "请登录");
        }

        // 1. 【核心逻辑】：直接从 Redis 获取缓存的用户信息
        // 这个 user 对象是在登录时存进去的，包含了 id, password, role 等所有信息
        Account user = (Account) redisTemplate.opsForValue().get("LOGIN_USER_" + token);

        if (user == null) {
            // Redis 里没数据，说明 Token 已失效（过期或用户已退出）
            throw new CustomException("401", "登录已失效，请重新登录");
        }

        try {
            // 2. 验证 JWT 签名
            // 注意：这里直接使用从 Redis 拿到的 user.getPassword()
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            jwtVerifier.verify(token);
        } catch (Exception e) {
            // 如果签名不对（比如用户在别处改了密码，或者 Token 被篡改）
            throw new CustomException("401", "身份验证失败，请重新登录");
        }

        return true;
    }
}