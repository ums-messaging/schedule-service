package com.ums.schedule.send.application.model.dto;

public record SendTargetUploadResult(
        Long successCount,
        Long failCount
) {
}
