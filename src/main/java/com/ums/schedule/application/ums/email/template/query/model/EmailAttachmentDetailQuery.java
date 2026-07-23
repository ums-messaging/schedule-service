package com.ums.schedule.application.ums.email.template.query.model;

import com.ums.schedule.adapter.api.request.email.request.EmailAttachmentRequest;

public record EmailAttachmentDetailQuery(
        String fileKey,
        String fileKeyTemplate,
        String attachmentName,
        String downloadName
) {
    public static EmailAttachmentDetailQuery of(EmailAttachmentRequest request) {
        return new EmailAttachmentDetailQuery(
                request.fileKeySuffix(),
                request.fileKeySuffixTemplate(),
                request.attachmentName(),
                request.downloadName()
        );
    }

    public EmailTemplateContext toContext(String fileKey, String fileKeyTemplate) {
        return EmailTemplateContext.of(this, fileKey, fileKeyTemplate);
    }
}
