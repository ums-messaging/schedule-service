package com.ums.schedule.email.application.template.query.model;


import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3FileMetadataResponse;

public record EmailTemplateContent(
        EmailMessageSection section,
        String contentType,
        String fileKey,
        Long fileSize
) {
    public static EmailTemplateContent of(EmailMessageSection section, AwsS3FileMetadataResponse response) {
        return new EmailTemplateContent(
                section,
                response.contentType(),
                response.key(),
                response.contentLength()
        );
    }
}
