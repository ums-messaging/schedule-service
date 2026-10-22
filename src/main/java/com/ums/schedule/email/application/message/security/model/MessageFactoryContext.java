package com.ums.schedule.email.application.message.security.model;

import com.ums.schedule.email.application.requests.result.SendRequestQueryResult;
import com.ums.schedule.email.domain.attachment.EmailAttachment;
import com.ums.schedule.email.domain.message.EmailSendMessage;
import com.ums.schedule.email.domain.template.EmailTemplate;

import java.util.List;


public record MessageFactoryContext(
        EmailSendMessage message,
        EmailTemplate template,
        List<EmailAttachment> attachments
) {
    public static MessageFactoryContext of(SendRequestQueryResult result) {
        return new MessageFactoryContext(
            result.message(),
            result.template(),
            result.attachmentList()
        );
    }
}
