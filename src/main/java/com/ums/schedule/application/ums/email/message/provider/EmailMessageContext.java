package com.ums.schedule.application.ums.email.message.provider;

import com.ums.schedule.application.ums.common.template.model.TemplateResult;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateResult;

import java.util.List;

public record EmailMessageContext(
        TemplateResult template,
        String title,
        String headerKey,
        String bodyKey,
        String footerKey,
        String imageDir,
        SecurityMail securityMail,
        List<ConvertedAttachment> attachments
) {

    public static EmailMessageContext of(EmailTemplateResult template, SecurityMail securityMail, EmailConvertPolicy policy) {
        TemplateResult templateResult = template.template();
        return new EmailMessageContext(
                templateResult,
                template.title(),
                template.headerKey(),
                policy.bodyKey(),
                template.footerKey(),
                template.imageDir(),
                securityMail,
                policy.combineAndGetAttachments(template)
        );
    }
}
