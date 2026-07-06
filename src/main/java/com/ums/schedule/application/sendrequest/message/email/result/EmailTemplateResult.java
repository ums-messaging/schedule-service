package com.ums.schedule.application.sendrequest.message.email.result;


import com.ums.schedule.application.sendrequest.template.result.TemplateResult;

public record EmailTemplateResult(
        TemplateResult template,
        EmailTemplateDetailResult emailTemplate
) {

    public static EmailTemplateResult of(EmailTemplateDetailResult detail) {
        return new EmailTemplateResult(null, detail);
    }
}
