package com.ums.schedule.application.sendrequest.template.result;

public record TemplateResult(
        String templateId,
        String templateName,
        String templateType,
        String channelType
) {
}
