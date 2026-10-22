package com.ums.schedule.email.application.target.reader.listener.model;

import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.group.DomainGroup;

import java.util.List;

public record SendTargetGroupedList(
    Long requestId,
    Integer groupNo,
    DomainGroup group,
    List<SendTargetRequest> targets
) {

    public static SendTargetGroupedList of(Integer groupNo, Long requestId, DomainGroup group, List<SendTargetRequest> targets) {
        return new SendTargetGroupedList(
                requestId,
                groupNo,
                group,
                targets
        );
    }
}
