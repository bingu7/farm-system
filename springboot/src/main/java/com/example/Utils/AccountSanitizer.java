package com.example.Utils;

import com.example.entity.Account;

import java.util.List;

public class AccountSanitizer {
    private AccountSanitizer() {
    }

    public static <T extends Account> T sanitize(T account) {
        if (account == null) {
            return null;
        }
        account.setPassword(null);
        account.setNewPassword(null);
        return account;
    }

    public static <T extends Account> List<T> sanitizeList(List<T> accounts) {
        if (accounts == null) {
            return null;
        }
        accounts.forEach(AccountSanitizer::sanitize);
        return accounts;
    }
}
