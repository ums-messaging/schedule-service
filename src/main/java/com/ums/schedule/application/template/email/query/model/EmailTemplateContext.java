package com.ums.schedule.application.template.email.query.model;

import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;

public record EmailTemplateContext(
        EmailTemplateSectionEnum section,
        String fileKey,
        String fileKeyTemplate,
        String attachmentName,
        String downloadName
) {
    public static EmailTemplateContext of(EmailTemplateSectionEnum section, EmailTemplateDetailQuery command, String fileKey) {
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
                EmailTemplateSectionEnum.ATTACHMENT,
                fileKey,
                fileKeyTemplate,
                command.attachmentName(),
                command.downloadName()
        );
    }
    public static EmailTemplateContext of(EmailTemplateSectionEnum section, EmailTemplateDetailQuery command, String fileKey, String fileKeyTemplate) {
        return new EmailTemplateContext(
                section,
                fileKey,
                fileKeyTemplate,
                command.attachmentName(),
                command.downloadName()
        );
    }


}
