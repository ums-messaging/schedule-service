package com.ums.schedule.email.adaptor.in.rest.message.response;

import com.ums.schedule.email.application.message.command.SendMessageCreateSummary;

public record SendMessageCreateResponse(
        String messageId,
        String convertType,
        String mailType,
        Integer attachmentCount
) {
    public static SendMessageCreateResponse of(SendMessageCreateSummary summary) {
        return new SendMessageCreateResponse(
                summary.messageId(),
                summary.convertType().code(),
                summary.emailType().code(),
                summary.attachmentCount()
        );
    }
}
