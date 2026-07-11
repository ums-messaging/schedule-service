package com.ums.schedule.application.ums.email.template.command;

public record EmailTemplateContentCommand(
        String fileKey,
        String template
) {
    public static EmailTemplateContentCommand of(String fileKey, String template) {
        return new EmailTemplateContentCommand(fileKey, template);
    }
}
