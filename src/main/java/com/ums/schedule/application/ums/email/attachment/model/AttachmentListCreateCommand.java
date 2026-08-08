package com.ums.schedule.application.ums.email.attachment.model;

import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.domain.message.email.EmailSendMessage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record AttachmentListCreateCommand(
        EmailSendMessage sendMessage,
        EmailConvertPolicy convertPolicy,
        List<AttachmentContext> attachments
) {
    public static AttachmentListCreateCommand of(EmailSendMessage message, EmailConvertPolicy convertPolicy, List<EmailTemplateContentResult> attachments) {
        List<AttachmentContext> attachmentContexts = Optional.ofNullable(attachments)
                .map(list -> list.stream()
                        .map(AttachmentContext::of)
                        .toList()
                ).orElseGet(() -> Collections.EMPTY_LIST);
        return new AttachmentListCreateCommand(
                message,
                convertPolicy,
                attachmentContexts
        );
    }
}
