package com.ums.schedule.adapter.api.request.email.response;

import com.ums.schedule.application.ums.email.request.model.EmailSendRequestCreateSummary;

public record EmailSendRequestCreateResponse(
        Long requestId,
        Integer retryCount,
        String messageId,
        String convertType,
        Integer attachmentCount
) {
    public static EmailSendRequestCreateResponse of(EmailSendRequestCreateSummary summary) {
        return new EmailSendRequestCreateResponse(
                summary.requestId(),
                summary.retryCount(),
                summary.messageId(),
                summary.convertType().code(),
                summary.attachmentCount()
        );
    }
}
