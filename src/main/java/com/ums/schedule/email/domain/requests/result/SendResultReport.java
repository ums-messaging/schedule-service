package com.ums.schedule.email.domain.requests.result;

import com.ums.schedule.email.domain.event.SendTargetResultList;

public record SendResultReport(
        int successCount,
        int failCount
) {
    public static SendResultReport of(SendResultReport report, SendTargetResultList resultList) {
        return new SendResultReport(
                report.successCount() + resultList.successList().size(),
                report.failCount() + resultList.errorList().size()
        );
    }
}
