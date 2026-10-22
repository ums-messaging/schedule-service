package com.ums.schedule.email.domain.requests.result;

import com.ums.schedule.email.application.target.reader.model.SendTargetsReadResult;

public record SendRequestReport(
        int totalCount,
        int groupCount,
        long successCount,
        long failCount
) {
    public static SendRequestReport of(SendTargetsReadResult result, long failCount) {
        return new SendRequestReport(
                result.totalCount(),
                result.groupCount(),
                result.totalCount() - failCount,
                failCount
        );
    }

    public static SendRequestReport initialize() {
        return new SendRequestReport(0, 0, 0,0);
    }
}
