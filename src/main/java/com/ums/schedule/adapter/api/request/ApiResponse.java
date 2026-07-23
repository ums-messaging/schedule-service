package com.ums.schedule.adapter.api.request;

import com.ums.schedule.common.code.api.ApiResponseCode;

import java.time.Instant;

public record ApiResponse<T> (
        String code,
        String status,
        String message,
        T details,
        Instant timestamp
) {
    public static <T> ApiResponse<T> of(ApiResponseCode code, Object details) {
        return new ApiResponse(
                code.code(),
                code.value(),
                code.description(),
                details,
                Instant.now()
        );
    }

}
