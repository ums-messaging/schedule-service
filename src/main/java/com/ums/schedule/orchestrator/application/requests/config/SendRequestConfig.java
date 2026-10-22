package com.ums.schedule.orchestrator.application.requests.config;

import com.ums.schedule.orchestrator.adaptor.in.config.properties.SendRequestProperties;

public record SendRequestConfig(
        int retryCount
) {
    public static SendRequestConfig of(SendRequestProperties properties) {
        return new SendRequestConfig(properties.getRetryCount());
    }
}
