package com.ums.schedule.application.ums.email.generator.model;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;

import java.util.List;

public record RenderedTemplate(
        ConvertType convertType,
        EmailType emailType,
        SecurityMail securityMail,
        RenderedTemplateContent header,
        RenderedTemplateContent body,
        RenderedTemplateContent footer,
        RenderedTemplateContent cover,
        List<RenderedTemplateContent> attachments
) {

    public static RenderedTemplate of(EmailTemplate template, TargetMessageData targetData) {
        return new RenderedTemplate(
                template.getConvertType(),
                template.getEmailType(),
                template.getSecurityMail(),
                template.renderHeader(targetData),
                template.renderBody(targetData),
                template.renderFooter(targetData),
                template.renderCover(targetData),
                template.renderAttachments(targetData)
        );
    }
}
