package com.zygoo132.first_app.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse<T> {
    private int code;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private T result;

    public static <T> ApiResponse<T> success(T result, String path) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("Success")
                .path(path)
                .timestamp(LocalDateTime.now())
                .result(result)
                .build();
    }

    public static ApiResponse<Object> error(int code, String message, String path) {
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .result(null)
                .build();
    }
}

