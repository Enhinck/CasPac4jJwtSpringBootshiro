package com.enhinck.config;

import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enhinck.demo.pojo.ValidationError;
import com.enhinck.util.ApiResponse;


@ControllerAdvice(basePackages = "com.enhinck.demo.api")
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BindException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse<Set<ValidationError>>> BindException(HttpServletRequest request,
                                                                           BindException exception) throws Exception {
        //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        Set<ValidationError> errors = exception.getBindingResult().getFieldErrors().stream().map(f -> new ValidationError()
                .setMessage(f.getDefaultMessage())
                .setName(f.getField())
                .setValue(f.getRejectedValue())).collect(Collectors.toSet());
        return ApiResponse.fail(400, "所提交信息错误", errors);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> IllegalArgumentException(IllegalArgumentException exception) {
        return ApiResponse.fail(400, exception.getMessage());
    }
}