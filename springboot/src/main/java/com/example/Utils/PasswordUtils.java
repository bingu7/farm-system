package com.example.Utils;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private PasswordUtils() {
    }

    public static String encode(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String storedPassword) {
        if (ObjectUtil.hasEmpty(rawPassword, storedPassword)) {
            return false;
        }
        if (isHashed(storedPassword)) {
            return ENCODER.matches(rawPassword, storedPassword);
        }
        return rawPassword.equals(storedPassword);
    }

    public static boolean needsUpgrade(String storedPassword) {
        return ObjectUtil.isNotEmpty(storedPassword) && !isHashed(storedPassword);
    }

    public static boolean isHashed(String password) {
        return ObjectUtil.isNotEmpty(password) && password.startsWith("$2");
    }
}
