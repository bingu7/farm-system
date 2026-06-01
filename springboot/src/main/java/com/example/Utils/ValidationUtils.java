package com.example.Utils;

import cn.hutool.core.util.ObjectUtil;
import com.example.exception.CustomException;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,20}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private ValidationUtils() {
    }

    public static void requireText(String value, String message) {
        if (ObjectUtil.isEmpty(value) || value.trim().isEmpty()) {
            throw new CustomException(message);
        }
    }

    public static void validateUsername(String username) {
        requireText(username, "用户名不能为空");
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new CustomException("用户名只能包含字母、数字、下划线，长度为3到20位");
        }
    }

    public static void validatePassword(String password) {
        requireText(password, "密码不能为空");
        if (password.length() < 3 || password.length() > 32) {
            throw new CustomException("密码长度必须为3到32位");
        }
    }

    public static void validatePhone(String phone) {
        if (ObjectUtil.isNotEmpty(phone) && !PHONE_PATTERN.matcher(phone).matches()) {
            throw new CustomException("手机号格式不正确");
        }
    }

    public static void validateEmail(String email) {
        if (ObjectUtil.isNotEmpty(email) && !EMAIL_PATTERN.matcher(email).matches()) {
            throw new CustomException("邮箱格式不正确");
        }
    }
}
