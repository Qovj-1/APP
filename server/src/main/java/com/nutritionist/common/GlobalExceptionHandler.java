package com.nutritionist.common;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ApiResult<Void> handleBiz(BusinessException e) {
    return ApiResult.fail(e.getCode(), e.getMessage());
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public ApiResult<Void> handleValid() {
    return ApiResult.fail(400, "参数错误");
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ApiResult<Void> handleBadCredentials() {
    return ApiResult.fail(401, "账号或密码错误");
  }

  @ExceptionHandler(Exception.class)
  public ApiResult<Void> handleOther(Exception e) {
    return ApiResult.fail(500, e.getMessage() != null ? e.getMessage() : "服务器错误");
  }
}
