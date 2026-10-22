package com.ums.schedule.orchestrator.adaptor.out.api.message.response;

import com.ums.schedule.email.application.message.query.result.SendMessageQueryResult;

public record EmailMessageResponse(
        String messageId,
        String header,
        String body,
        String footer,
        String cover,
        String imageDir,
        String from,
        String fromName
) {
    public static EmailMessageResponse of(SendMessageQueryResult result) {
        return new EmailMessageResponse(
                result.messageId(),
                result.header(),
                result.body(),
                result.footer(),
                result.cover(),
                result.imageDir(),
                "admin@admin.com",
                "admin"
        );
    }
}
