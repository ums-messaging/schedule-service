package com.ums.schedule.application.ums.email.message.model;

import com.ums.schedule.application.ums.email.message.provider.EmailPolicyResult;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateResult;
import com.ums.schedule.domain.message.email.convert.ConvertMail;

public record EmailMessageCreateContext(
        String title,
        String headerKey,
        String bodyKey,
        String coverKey,
        String footerKey,
        String imageDir,
        SecurityMail securityMail,
        ConvertMail convertMail
) {
    public static EmailMessageCreateContext of(EmailTemplateResult template, EmailPolicyResult policy) {
        return new EmailMessageCreateContext(
                template.title(),
                template.headerKey(),
                template.bodyKey(),
                template.coverKey(),
                template.footerKey(),
                template.imageDir(),
                policy.securityMail(),
                policy.convertMail()
        );
    }

}
