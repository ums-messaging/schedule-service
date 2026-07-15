package com.ums.schedule.application.ums.common.request.model;

import com.ums.schedule.application.sendrequest.target.result.TargetUploadResult;
import com.ums.schedule.domain.request.SendRequest;

public record SendRequestCreateResult(
        SendRequest sendRequest,
        TargetUploadResult targetUploadReport
) {

    public static SendRequestCreateResult of(SendRequest sendRequest, TargetUploadResult result) {
        return new SendRequestCreateResult(
                sendRequest,
                result);
    }
}
