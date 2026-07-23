package com.ums.schedule.adapter.api.request.email.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ums.schedule.adapter.api.request.response.TargetUploadResponse;
import com.ums.schedule.application.ums.email.request.model.EmailSendRequestCreateSummary;

public record JsonEmailSendRequestResponse(
        @JsonUnwrapped
        EmailSendRequestCreateResponse requestResponse,
        TargetUploadResponse currentTargetUpload
) {
    public static JsonEmailSendRequestResponse of(EmailSendRequestCreateSummary summary) {
        EmailSendRequestCreateResponse requestResponse = EmailSendRequestCreateResponse.of(summary);
        TargetUploadResponse uploadResponse = TargetUploadResponse.of(summary.targetUploadSummary());
        return new JsonEmailSendRequestResponse(
                requestResponse,
                uploadResponse
        );
    }
}
