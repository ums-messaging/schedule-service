package com.ums.schedule.adapter.api.request.email.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ums.schedule.adapter.api.request.response.FileSendRequestResponse;
import com.ums.schedule.application.ums.email.request.model.EmailSendRequestCreateSummary;

public record FileEmailSendRequestResponse(
        @JsonUnwrapped
        EmailSendRequestCreateResponse sendRequest,
        FileSendRequestResponse currentTargetUpload
) {
    public static FileEmailSendRequestResponse of(EmailSendRequestCreateSummary summary) {
        EmailSendRequestCreateResponse requestResponse = EmailSendRequestCreateResponse.of(summary);
        FileSendRequestResponse uploadResponse = FileSendRequestResponse.of(summary.targetUploadSummary());
        return new FileEmailSendRequestResponse(
                requestResponse,
                uploadResponse
        );
    }
}
