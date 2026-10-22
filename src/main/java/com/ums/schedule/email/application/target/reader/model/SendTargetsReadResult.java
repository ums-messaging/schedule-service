package com.ums.schedule.email.application.target.reader.model;

public record SendTargetsReadResult(
    Integer groupCount,
    Integer totalCount,
    Integer successCount,
    Integer failCount
) {
    public static SendTargetsReadResult of(SendTargetReadContext listener) {
        return new SendTargetsReadResult(
                listener.getGroupCount(),
                listener.getTotalCount(),
                listener.getSuccessCount(),
                listener.getFailCount()
        );
    }
}
