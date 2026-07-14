package com.ums.schedule.application.ums.email.template.query.model;


import com.ums.schedule.application.ums.common.template.TemplateResult;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.common.code.email.EmailTemplateSectionEnum;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

public record EmailTemplateResult(
        TemplateResult template,
        EmailTemplateDetailResult emailTemplate
) {

    public static EmailTemplateResult of(EmailTemplateDetailResult detail) {
        return new EmailTemplateResult(null, detail);
    }

    public String title() {
        return emailTemplate.msgTitle();
    }

    public AttachmentContext headerTemplate() {
        EmailTemplateContentResult header = emailTemplate.getHeaderFooter().get(EmailTemplateSectionEnum.HEADER);
        return getTemplate(header);
    }

    public AttachmentContext footerTemplate() {
        EmailTemplateContentResult footer = emailTemplate.getHeaderFooter().get(EmailTemplateSectionEnum.FOOTER);
        return getTemplate(footer);
    }

    public AttachmentContext coverTemplate() {
        EmailTemplateContentResult cover = emailTemplate.getHeaderFooter().get(EmailTemplateSectionEnum.COVER);
        return getTemplate(cover);
    }

    public AttachmentContext bodyTemplate() {
        EmailTemplateContentResult body = emailTemplate.getBody();
        return getTemplate(body);
    }

    public String headerKey() {
        EmailTemplateContentResult header = emailTemplate.getHeaderFooter().get(EmailTemplateSectionEnum.HEADER);
        return getFileKey(header);
    }

    public String bodyKey() {
        EmailTemplateContentResult body = emailTemplate.getBody();
        return getFileKey(body);
    }

    public String footerKey() {
        EmailTemplateContentResult footer = emailTemplate.getHeaderFooter().get(EmailTemplateSectionEnum.FOOTER);
        return getFileKey(footer);
    }

    public String coverKey() {
        EmailTemplateContentResult cover = emailTemplate.getHeaderFooter().get(EmailTemplateSectionEnum.COVER);
        return getFileKey(cover);
    }

    private AttachmentContext getTemplate(EmailTemplateContentResult content) {
        return Optional.ofNullable(content)
                .map(AttachmentContext::of)
                .orElse(null);
    }

    private String getFileKey(EmailTemplateContentResult content) {
        return Optional.ofNullable(content)
                .map(EmailTemplateContentResult::fileKey)
                .filter(StringUtils::hasText)
                .orElse(null);
    }

    public String imageDir() {
        return emailTemplate.imageDir();
    }

    private String getFileKeyTemplate(EmailTemplateContentResult content) {
        return Optional.ofNullable(content)
                .map(EmailTemplateContentResult::fileKeyTemplate)
                .filter(StringUtils::hasText)
                .orElse(null);
    }

    public List<ConvertedAttachment> convertAndAttachments() {
        return emailTemplate.getAttachmentList().stream()
                .map(ConvertedAttachment::of)
                .toList();
    }

}
