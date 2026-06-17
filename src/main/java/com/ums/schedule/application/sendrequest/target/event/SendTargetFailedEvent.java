package com.ums.schedule.application.sendrequest.target.event;

import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.domain.sendrequest.target.SendTarget;

import java.util.ArrayList;
import java.util.List;

public record SendTargetFailedEvent(
        List<SendTarget> failureTargetList
) {

    public static SendTargetFailedEvent of(List<SendTargetSaveResult> results) {
        List<SendTarget> targetList = new ArrayList<>();
        for (SendTargetSaveResult result : results) {
            for (SendTarget sendTarget : result.failedTargetList()) {
                targetList.add(sendTarget);
            }
        }
        return new SendTargetFailedEvent(targetList);
    }
}
