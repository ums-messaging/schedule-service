package com.ums.schedule.application.ums.email.template.query.model;


import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.common.code.common.FileContentType;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.common.code.email.EmailTemplateFormat;

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
                EmailTemplateFormat.FILE.value(),
                context.section().code(),
                FileContentType.HTML.code(),
                context.attachmentName(),
                context.downloadName(),
                context.fileKeyTemplate(),
                context.fileKey(),
                metadata.contentLength()
        );
    }
    public static EmailTemplateContentResult of(EmailTemplateContext context) {
        return new EmailTemplateContentResult(
                EmailTemplateFormat.FILE.value(),
                context.section().code(),
                FileContentType.HTML.code(),
                context.attachmentName(),
                context.downloadName(),
                context.fileKeyTemplate(),
                context.fileKey(),
                null
        );
    }
    public static EmailTemplateContentResult of(EmailMessageSection section, EmailAttachmentDetailQuery command, AwsS3FileMetadataResponse response) {
        return new EmailTemplateContentResult(
                EmailTemplateFormat.FILE.code(),
                section.code(),
                null,
                null,
                null,
                null,
                response.key(),
                response.contentLength()
        );
    }

    public static EmailTemplateContentResult of(EmailAttachmentDetailQuery query, String fileKey, AwsS3FileMetadataResponse metadata) {
        return new EmailTemplateContentResult(
                EmailTemplateFormat.FILE.value(),
                EmailMessageSection.ATTACHMENT.code(),
                metadata.contentType(),
                query.attachmentName(),
                query.downloadName(),
                null,
                fileKey,
                metadata.contentLength()
        );
    }

    public static EmailTemplateContentResult of(EmailAttachmentDetailQuery query, String fileKey) {
        return new EmailTemplateContentResult(
                EmailTemplateFormat.FILE.value(),
                EmailMessageSection.ATTACHMENT.code(),
                null,
                query.attachmentName(),
                query.downloadName(),
                fileKey,
                null,
                null
        );
    }
}
