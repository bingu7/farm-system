package com.example.Utils;

import com.example.entity.Account;
import com.example.exception.CustomException;

import java.util.Objects;

public class AuthUtils {
    private AuthUtils() {
    }

    public static Account getCurrentUser() {
        Account account = LoginUserHolder.get();
        if (account == null) {
            throw new CustomException("401", "请登录");
        }
        return account;
    }

    public static void requireAdmin() {
        if (!isAdmin(getCurrentUser())) {
            throw new CustomException("403", "无权限操作");
        }
    }

    public static void requireSelfOrAdmin(Integer userId) {
        Account currentUser = getCurrentUser();
        if (isAdmin(currentUser)) {
            return;
        }
        if (!Objects.equals(currentUser.getId(), userId)) {
            throw new CustomException("403", "无权限操作");
        }
    }

    public static boolean isAdmin(Account account) {
        return account != null && "ADMIN".equals(account.getRole());
    }
}
