package com.example.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.example.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get();

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Result> customError(CustomException e) {
        return ResponseEntity.status(toHttpStatus(e.getCode()))
                .body(Result.error(e.getCode(), e.getMsg()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Result> maxUploadSizeError(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Result.error("413", "上传文件不能超过5MB"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> error(Exception e) {
        log.error("异常信息：", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error());
    }

    private HttpStatus toHttpStatus(String code) {
        if ("401".equals(code)) {
            return HttpStatus.UNAUTHORIZED;
        }
        if ("403".equals(code)) {
            return HttpStatus.FORBIDDEN;
        }
        if ("400".equals(code)) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
