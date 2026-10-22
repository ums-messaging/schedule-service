package com.ums.schedule.email.adaptor.in.rest.template.response;

import com.ums.schedule.email.domain.template.EmailTemplate;

import java.time.LocalDateTime;

public record TemplateCreateResponse(
        String templateId,
        String headerKey,
        Long headerSize,
        String bodyKey,
        Long bodySize,
        String footerKey,
        Long footerSize,
        String coverKey,
        Long coverSize,
        String imageDir,
        LocalDateTime createdAt
) {
    public static TemplateCreateResponse of(EmailTemplate template) {
        return new TemplateCreateResponse(
                template.getId(),
                template.getHeaderKey(),
                template.getHeaderSize(),
                template.getBodyKey(),
                template.getBodySize(),
                template.getFooterKey(),
                template.getFooterSize(),
                template.getCoverKey(),
                template.getCoverSize(),
                template.getImageDir(),
                template.getCreatedAt()
        );
    }
}
