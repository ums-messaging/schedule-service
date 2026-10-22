package com.ums.schedule.email.application.template.query.model;

import com.ums.schedule.common.code.email.EmailMessageSection;

import java.util.Map;

public record EmailTemplateFileResult(
        Map<EmailMessageSection, EmailTemplateContent> templateContentMap,
        String imageDir
) {
    public static EmailTemplateFileResult of(Map<EmailMessageSection, EmailTemplateContent> templateContentMap, String imageDir) {
        return new EmailTemplateFileResult(templateContentMap, imageDir);
    }
}
