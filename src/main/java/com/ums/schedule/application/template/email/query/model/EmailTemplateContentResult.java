package com.ums.schedule.application.template.email.query.model;


import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.domain.send.email.code.ContentTypeEnum;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
import com.ums.schedule.domain.sendrequest.template.email.code.TemplateContentFormatEnum;

public record EmailTemplateContentResult(
        String format,
        String section,
        String contentType,
        String attachmentName,
        String downloadName,
        String fileKeyTemplate,
        String fileKey,
        Long fileSize
) {
    public static EmailTemplateContentResult of(EmailTemplateContext context,
                                                AwsS3FileMetadataResponse metadata) {
        return new EmailTemplateContentResult(
                TemplateContentFormatEnum.FILE.value(),
                context.section().code(),
                ContentTypeEnum.HTML.code(),
                context.attachmentName(),
                context.downloadName(),
                context.fileKeyTemplate(),
                context.fileKey(),
                metadata.contentLength()
        );
    }
    public static EmailTemplateContentResult of(EmailTemplateContext context) {
        return new EmailTemplateContentResult(
                TemplateContentFormatEnum.FILE.value(),
                context.section().code(),
                ContentTypeEnum.HTML.code(),
                context.attachmentName(),
                context.downloadName(),
                context.fileKeyTemplate(),
                context.fileKey(),
                null
        );
    }
    public static EmailTemplateContentResult of(EmailTemplateSectionEnum section, EmailAttachmentDetailQuery command, AwsS3FileMetadataResponse response) {
        return new EmailTemplateContentResult(
                TemplateContentFormatEnum.FILE.code(),
                section.code(),
                null,
                null,
                null,
                null,
                response.key(),
                response.contentLength()
        );
    }

    public static EmailTemplateContentResult of(String fileKey) {
        return new EmailTemplateContentResult(
                TemplateContentFormatEnum.FILE.value(),
                EmailTemplateSectionEnum.ATTACHMENT.code(),
                null,
                null,
                null,
                null,
                fileKey,
                0L
        );
    }


}
