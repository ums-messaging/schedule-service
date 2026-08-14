package com.ums.schedule.application.ums.common.template.model;

public record TemplateResult(
        String templateId,
        String templateName,
        String templateType,
        String channelType
) {
    public static TemplateResult of(String templateKey, String templateType) {
        return new TemplateResult(
                templateKey,
                null,
                templateType,
                null
        );
    }
}
