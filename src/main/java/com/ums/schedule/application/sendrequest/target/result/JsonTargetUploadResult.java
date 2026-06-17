package com.ums.schedule.application.sendrequest.target.result;

public record JsonTargetUploadResult(
        Long totalCount,
        Long successCount,
        Long failCount
) {
    protected static JsonTargetUploadResult of(Long totalCount, Long successCount, Long failCount) {
        return new JsonTargetUploadResult(totalCount, successCount, failCount);
    }
}
