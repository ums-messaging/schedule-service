package com.ums.schedule.orchestrator.application.event.manager.handler.model;

import com.ums.schedule.email.application.send.job.model.SendJob;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.group.DomainGroup;

import java.util.List;


public record SendTargetRequestedEvent(
        SendJob job,
        Long groupId,
        String groupKey,
        Integer totalCount
) {

    public static SendTargetRequestedEvent of(SendJob job, DomainGroup group, Integer totalCount) {
        return new SendTargetRequestedEvent(
                job, group.groupId(), group.groupKey(), totalCount
        );
    }
}
