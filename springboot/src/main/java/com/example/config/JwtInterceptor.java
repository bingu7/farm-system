package com.example.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.example.Utils.LoginUserHolder;
import com.example.Utils.TokenUtils;
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
    @Resource
    private TokenUtils tokenUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        if (ObjectUtil.isEmpty(token)) {
            throw new CustomException("401", "请登录");
        }

        try {
            JWTVerifier jwtVerifier = JWT.require(tokenUtils.getAlgorithm()).build();
            jwtVerifier.verify(token);
        } catch (Exception e) {
            throw new CustomException("401", "身份验证失败，请重新登录");
        }

        Account user = (Account) redisTemplate.opsForValue().get("LOGIN_USER_" + token);
        if (user == null) {
            throw new CustomException("401", "登录已失效，请重新登录");
        }

        String tokenUserId = JWT.decode(token).getAudience().isEmpty() ? null : JWT.decode(token).getAudience().get(0);
        if (ObjectUtil.isEmpty(tokenUserId) || !tokenUserId.equals(String.valueOf(user.getId()))) {
            throw new CustomException("401", "身份验证失败，请重新登录");
        }

        LoginUserHolder.set(user);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LoginUserHolder.clear();
    }
}
