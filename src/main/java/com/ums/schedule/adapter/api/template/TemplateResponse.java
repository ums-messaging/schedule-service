package com.ums.schedule.adapter.api.template;

public record TemplateResponse(
        String templateId,
        String templateName,
        String templateType,
        String channelType
) {
}
