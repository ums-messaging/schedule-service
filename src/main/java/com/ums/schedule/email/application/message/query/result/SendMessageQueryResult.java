package com.ums.schedule.email.application.message.query.result;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.email.domain.template.EmailTemplate;

import java.util.UUID;

public record SendMessageQueryResult(
        String messageId,
        String subject,
        String header,
        String body,
        String footer,
        String cover,
        String imageDir
) {
    public static SendMessageQueryResult of(UUID messageId, String subject, EmailTemplate template) {
        return new SendMessageQueryResult(
                UuidCreator.toString(messageId),
                subject,
                template.getHeader().fileKey(),
                template.getBody().fileKey(),
                template.getFooter().fileKey(),
                template.getCover().fileKey(),
                template.getImageDir()
        );
    }
}
