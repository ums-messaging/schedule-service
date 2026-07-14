package com.ums.schedule.adapter.api.request.response;

import com.ums.schedule.application.message.email.result.EmailMessageResult;
import com.ums.schedule.application.sendrequest.result.SendRequestCreateResult;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResult;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.sendrequest.SendRequest;

public record EmailSendRequestCreateResponse (
        SendRequestCreateResult sendRequest,
        EmailMessageResult message
) {
    public static EmailSendRequestCreateResponse of(SendRequest sendRequest, EmailSendMessage message, TargetUploadResult result) {
        SendRequestCreateResult response = SendRequestCreateResult.of(sendRequest, result);
        return new EmailSendRequestCreateResponse(response, null);
    }
}