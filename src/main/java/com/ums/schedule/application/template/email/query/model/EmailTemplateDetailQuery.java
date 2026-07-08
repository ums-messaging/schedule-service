package com.ums.schedule.application.template.email.query.model;

import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record EmailTemplateDetailQuery(
        String customerId,
        String templateKey,
        String messageType,
        String title,
        String attachmentName,
        String downloadName,
        List<EmailAttachmentDetailQuery> attachmentKeyList
) {
    public static EmailTemplateDetailQuery of(String customerId, EmailSendCreateRequest request) {
        return new EmailTemplateDetailQuery(
                customerId,
                request.request().templateKey(),
                request.request().messageType(),
                request.title(),
                request.attachmentNameFormat(),
                request.downloadNameFormat(),
                Optional.ofNullable(request.attachmentList())
                        .map(list -> list.stream()
                                .map(EmailAttachmentDetailQuery::of)
                                .toList()
                        )
                        .orElse(Collections.EMPTY_LIST)
        );
    }

    public EmailTemplateContext toContext(EmailTemplateSectionEnum section, String fileKey) {
        return EmailTemplateContext.of(section, this, fileKey);
    }

}
