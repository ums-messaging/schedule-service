package com.ums.schedule.application.template.email.query.model;


import com.ums.schedule.application.template.result.TemplateResult;

public record EmailTemplateResult(
        TemplateResult template,
        EmailTemplateDetailResult emailTemplate
) {

    public static EmailTemplateResult of(EmailTemplateDetailResult detail) {
        return new EmailTemplateResult(null, detail);
    }
}
