package com.ums.schedule.email.application.target.query.model;

import com.ums.schedule.email.application.send.job.model.SendJob;
import com.ums.schedule.email.domain.target.group.DomainGroup;

public record SendTargetGroupQuery(
        Long sendRequestId,
        Long groupId,
        String groupKey
) {

    public static SendTargetGroupQuery of(SendJob job, DomainGroup group) {
        return new SendTargetGroupQuery(job.sendRequestId(), group.groupId(), group.groupKey());
    }
}
