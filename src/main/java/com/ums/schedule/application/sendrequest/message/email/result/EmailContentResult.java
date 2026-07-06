package com.ums.schedule.application.sendrequest.message.email.result;


import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.adapter.api.request.request.EmailAttachmentRequest;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
import com.ums.schedule.domain.sendrequest.template.email.code.TemplateContentFormatEnum;

public record EmailContentResult(
        String format,
        String section,
        String contentType,
        String attachmentName,
        String downloadName,
        String baseDir,
        String fileKeyTemplate,
        String fileKey,
        Long fileSize
) {

    public static EmailContentResult of(EmailTemplateSectionEnum section, AwsS3FileMetadataResponse response) {
        return new EmailContentResult(
                TemplateContentFormatEnum.FILE.value(),
                section.code(),
                null,
                null,
                null,
                null,
                null,
                response.key(),
                response.contentLength()
        );
    }
    public static EmailContentResult of(EmailAttachmentRequest request) {
        return new EmailContentResult(
                TemplateContentFormatEnum.FILE.value(),
                EmailTemplateSectionEnum.ATTACHMENT.code(),
                null,
                request.attachmentName(),
                request.downloadName(),
                null,
                request.fileKeyTemplate(),
                request.fileKey(),
                0L
        );
    }
}
