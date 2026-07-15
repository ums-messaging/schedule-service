package com.ums.schedule.application.ums.email.template.query.model;

import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.common.code.email.EmailMessageSection;

public record EmailTemplateContext(
        EmailMessageSection section,
        String fileKey,
        String fileKeyTemplate,
        String attachmentName,
        String downloadName
) {
    public static EmailTemplateContext of(EmailMessageSection section, EmailTemplateDetailQuery command, String fileKey) {
        return new EmailTemplateContext(
                section,
                fileKey,
                null,
                command.attachmentName(),
                command.downloadName()
        );
    }
    public EmailTemplateContentResult toContent(AwsS3FileMetadataResponse metadata) {
        return EmailTemplateContentResult.of(this, metadata);
    }
    public static EmailTemplateContext of(EmailAttachmentDetailQuery command, String fileKey, String fileKeyTemplate) {
        return new EmailTemplateContext(
                EmailMessageSection.ATTACHMENT,
                fileKey,
                fileKeyTemplate,
                command.attachmentName(),
                command.downloadName()
        );
    }
    public static EmailTemplateContext of(EmailMessageSection section, EmailTemplateDetailQuery command, String fileKey, String fileKeyTemplate) {
        return new EmailTemplateContext(
                section,
                fileKey,
                fileKeyTemplate,
                command.attachmentName(),
                command.downloadName()
        );
    }


}
