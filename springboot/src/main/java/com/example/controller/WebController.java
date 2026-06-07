package com.example.controller;

import com.example.common.Result;
import com.example.Utils.AccountSanitizer;
import com.example.Utils.AuthUtils;
import com.example.Utils.TokenUtils;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.service.AdminService;
import com.example.service.StatisticsService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;


@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Resource
    private StatisticsService statisticsService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private TokenUtils tokenUtils;

    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    /**
     * 登录
     */
/*    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account ac = null;
        if ("ADMIN".equals(account.getRole())) {
            ac = adminService.login(account);
        } else {
            ac = userService.login(account);
        }
        return Result.success(ac);
    }*/
    /**
     * 登录
     * 加了jwt认证
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account ac = "ADMIN".equals(account.getRole()) ? adminService.login(account) : userService.login(account);

        // 登录成功后生成 Token
        String token = tokenUtils.createToken(ac.getId().toString(), ac.getRole());
        ac.setToken(token);
        AccountSanitizer.sanitize(ac);

        redisTemplate.opsForValue().set("LOGIN_USER_" + token, ac, 1, TimeUnit.DAYS);

        return Result.success(ac);
    }

    // 新增退出登录接口
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token != null) {
            redisTemplate.delete("LOGIN_USER_" + token);
        }
        return Result.success();
    }



    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(HttpServletRequest request, @RequestBody Account account) {
        Account currentUser = AuthUtils.getCurrentUser();
        account.setUsername(currentUser.getUsername());
        account.setRole(currentUser.getRole());
        if ("ADMIN".equals(currentUser.getRole())) {
            adminService.updatePassword(account);
        } else {
            userService.updatePassword(account);
        }
        String token = request.getHeader("token");
        if (token != null) {
            redisTemplate.delete("LOGIN_USER_" + token);
        }
        return Result.success();
    }
    /**
     * 统计
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        Account currentUser = AuthUtils.getCurrentUser();
        return Result.success(statisticsService.getStatistics(currentUser.getId(), currentUser.getRole()));
    }

}
