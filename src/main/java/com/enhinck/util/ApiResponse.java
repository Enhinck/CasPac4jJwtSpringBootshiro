package com.enhinck.util;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.ResponseEntity;


@Accessors(chain = true)
@Getter
@Setter
public class ApiResponse<T> {
    private int code = 200;
    private String message = "";
    private T data;

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(new ApiResponse<T>().setData(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(int code, String message) {
        return ResponseEntity.ok(new ApiResponse<T>().setCode(code).setMessage(message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(int code, String message, T data) {
        return ResponseEntity.ok(new ApiResponse<T>().setCode(code).setMessage(message).setData(data));
    }
}
