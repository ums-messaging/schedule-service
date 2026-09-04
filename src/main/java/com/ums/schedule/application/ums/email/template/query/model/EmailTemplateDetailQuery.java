package com.ums.schedule.application.ums.email.template.query.model;

import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record EmailTemplateDetailQuery (
        String customerId,
        String templateKey,
        String messageType,
        String title,
        String attachmentName,
        String downloadName,
        List<EmailAttachmentDetailQuery> attachmentQueries
) {
    public static EmailTemplateDetailQuery of(String customerId,
                                              EmailSendCreateRequest request,
                                              List<EmailAttachmentDetailQuery> queries) {
        return new EmailTemplateDetailQuery(
                customerId,
                request.request().templateKey(),
                request.request().messageType(),
                request.title(),
                request.attachmentName(),
                request.downloadName(),
                queries
        );
    }

    public EmailTemplateContext toContext(EmailMessageSection section, String fileKey) {
        return EmailTemplateContext.of(section, this, fileKey);
    }
}
