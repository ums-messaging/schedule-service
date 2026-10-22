package com.ums.schedule.email.application.template.command;


import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.email.adaptor.in.rest.template.request.EmailTemplateCreateRequest;
import com.ums.schedule.email.application.template.query.model.EmailTemplateContent;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3FileMetadataResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record EmailTemplateCreateCommand(
    String customerId,
    String templateKey,
    String defaultImageDir,
    String imageDir,
    Long headerSize,
    String headerKey,
    Long bodySize,
    String bodyKey,
    Long footerSize,
    String footerKey,
    Long coverSize,
    String coverKey
) {

    public static EmailTemplateCreateCommand of(String customerId,
                                                EmailTemplateCreateRequest request,
                                                Map<EmailMessageSection, EmailTemplateContent> templateMap,
                                                String defaultImageDir) {
        EmailTemplateContent header = templateMap.get(EmailMessageSection.HEADER);
        EmailTemplateContent cover = templateMap.get(EmailMessageSection.COVER);
        EmailTemplateContent body = templateMap.get(EmailMessageSection.BODY);
        EmailTemplateContent footer = templateMap.get(EmailMessageSection.FOOTER);

        return new EmailTemplateCreateCommand(
                customerId,
                request.templateKey(),
                defaultImageDir,
                request.imageDir(),
                Optional.ofNullable(header).map(EmailTemplateContent::fileSize).orElse(null),
                Optional.ofNullable(header).map(EmailTemplateContent::fileKey).orElse(null),
                Optional.ofNullable(body).map(EmailTemplateContent::fileSize).orElse(null),
                Optional.ofNullable(body).map(EmailTemplateContent::fileKey).orElse(null),
                Optional.ofNullable(footer).map(EmailTemplateContent::fileSize).orElse(null),
                Optional.ofNullable(footer).map(EmailTemplateContent::fileKey).orElse(null),
                Optional.ofNullable(cover).map(EmailTemplateContent::fileSize).orElse(null),
                Optional.ofNullable(cover).map(EmailTemplateContent::fileKey).orElse(null)
                );
    }
}
