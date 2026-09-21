package com.ums.schedule.application.ums.common.send.model;

public record EmailTargetGroupQueryResult(
        Long groupId,
        String emailDomain,
        Long count
) {
}
