package com.ums.schedule.email.domain.requests.result;

import com.ums.schedule.email.domain.event.SendTargetResultList;

public record SendResultReport(
        int successCount,
        int failCount
) {
    public static SendResultReport of(SendTargetResultList resultList) {
        return new SendResultReport(resultList.successList().size(), resultList.errorList().size());
    }
}
