package com.ums.schedule.application.sendrequest.target.event;


import com.ums.schedule.common.code.target.SendTargetStatus;
import com.ums.schedule.domain.target.TargetMessage;

import java.util.List;

public record SendTargetFailedEvent(
        List<TargetMessage> failureTargetList
) {

    public static SendTargetFailedEvent of(List<TargetMessage> results) {
        List<TargetMessage> targetList = results
                .stream()
                .filter(v -> v.getState() == SendTargetStatus.FAIL)
                .toList();
        return new SendTargetFailedEvent(targetList);
    }
}
