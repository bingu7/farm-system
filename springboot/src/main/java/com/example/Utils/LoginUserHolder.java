package com.example.Utils;

import com.example.entity.Account;

public class LoginUserHolder {
    private static final ThreadLocal<Account> HOLDER = new ThreadLocal<>();

    private LoginUserHolder() {
    }

    public static void set(Account account) {
        HOLDER.set(account);
    }

    public static Account get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
