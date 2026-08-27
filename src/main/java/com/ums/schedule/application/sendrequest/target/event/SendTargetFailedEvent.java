package com.ums.schedule.application.sendrequest.target.event;

import com.ums.schedule.application.sendrequest.target.result.SendTargetUploadResult;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResultList;
import com.ums.schedule.common.code.target.SendTargetStatus;
import com.ums.schedule.domain.target.TargetMessage;

import java.util.List;

public record SendTargetFailedEvent(
        List<TargetMessage> failureTargetList
) {

    public static SendTargetFailedEvent of(List<TargetMessage> results) {
        List<TargetMessage> targetList = results
                .stream()
                .filter(v -> v.getState().getCurrentCode() == SendTargetStatus.FAIL)
                .toList();
        return new SendTargetFailedEvent(targetList);
    }
}
