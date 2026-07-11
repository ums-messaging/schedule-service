package com.ums.schedule.application.ums.email.template.query.model;


import com.ums.schedule.application.ums.common.template.TemplateResult;

public record EmailTemplateResult(
        TemplateResult template,
        EmailTemplateDetailResult emailTemplate
) {

    public static EmailTemplateResult of(EmailTemplateDetailResult detail) {
        return new EmailTemplateResult(null, detail);
    }
}
