package com.navaneeth.multitenantgstplatform.common.factory;

import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ApiResponseFactory {

    public <T> ApiResponse<T> success(
            T data,
            String message
    ) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
