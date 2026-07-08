package com.ums.schedule.application.template.email.command;

public record EmailTemplateContentCommand(
        String fileKey,
        String template
) {
    public static EmailTemplateContentCommand of(String fileKey, String template) {
        return new EmailTemplateContentCommand(fileKey, template);
    }
}
