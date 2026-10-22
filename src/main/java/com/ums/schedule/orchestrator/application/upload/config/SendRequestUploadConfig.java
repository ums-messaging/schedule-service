package com.ums.schedule.orchestrator.application.upload.config;

public record SendRequestUploadConfig(
        String bucket,
        String uploadDir
) {
}
