package com.ums.schedule.application.sendrequest.target.result;

import com.ums.schedule.common.code.target.SendTargetRowStatus;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;

public record SendTargetUploadResult(
        TargetMessage target,
        SendTargetRowStatus status,
        String resultMessage
) {
    public static SendTargetUploadResult of(TargetMessage target) {
        return new SendTargetUploadResult(target, SendTargetRowStatus.SUCCESS, null);
    }

    public static SendTargetUploadResult of(TargetMessage target, String errorMessage) {
        return new SendTargetUploadResult(target, SendTargetRowStatus.FAIL, errorMessage);
    }
}
