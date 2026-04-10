package com.ums.schedule.template.application.response;

public record TemplateResponse(
        String templateId,
        String templateName,
        String templateType,
        String channelType
) {
}
