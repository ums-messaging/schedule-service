package com.ums.schedule.common.mapper;

import com.ums.schedule.email.application.requests.result.SendRequestSaveResult;

public record EmailSendRequestResponse(
        Long requestId
) {
    public static EmailSendRequestResponse of(SendRequestSaveResult result) {
        return new EmailSendRequestResponse(
                result.sendRequestId()
        );
    }
}
