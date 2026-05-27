package com.example.exception;

public class CustomException extends RuntimeException {
    private String msg;
    private String code;

    // 保留原来的构造方法，默认 code 为 500 (代表普通的业务逻辑错误)
    public CustomException(String msg) {
        this.msg = msg;
        this.code = "500";
    }

    // 新增一个支持传入 code 的构造方法 (专门给 JWT 拦截器用，传 401)
    public CustomException(String code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }
    public String getCode() { return code; }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
