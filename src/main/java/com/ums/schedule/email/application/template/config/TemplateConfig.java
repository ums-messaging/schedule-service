package com.ums.schedule.email.application.template.config;

import com.ums.schedule.email.adaptor.in.config.properties.TemplateProperties;

public record TemplateConfig(
    String uploadDir,
    String imageDir,
    String attachmentsDir
) {
    public static TemplateConfig toConfig(TemplateProperties properties) {
        return new TemplateConfig(
                properties.getUploadDir(),
                properties.getImageDir(),
                properties.getAttachmentsDir()
        );
    }
}
