package com.ums.schedule.application.message.email.command;

import com.ums.schedule.application.sendrequest.message.email.command.EmailSendMessageCreateCommand;
import com.ums.schedule.application.template.email.command.EmailTemplateContentCommand;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EmailSendMessageCreateCommandBuilder {
    private String uploadPrefix;
    private String title;

    private EmailTemplateContentCommand header;
    private EmailTemplateContentCommand body;
    private EmailTemplateContentCommand footer;

    public static EmailSendMessageCreateCommandBuilder builder() {
        return new EmailSendMessageCreateCommandBuilder();
    }
    private EmailSendMessageCreateCommandBuilder() {
        this.uploadPrefix = "/template/upload";
        this.title = "email message subject";
        this.body = givenDefaultEmailBody();
    }

    public EmailSendMessageCreateCommandBuilder title(String title) {
        this.title = title;
        return this;
    }

    public EmailSendMessageCreateCommandBuilder header(String key, String template) {
        this.header = (key == null && template == null) ?
                null : EmailTemplateContentCommand.of(key, template);
        return this;
    }

    public EmailSendMessageCreateCommandBuilder body(String key, String template) {
        this.body = (key == null && template == null) ?
                null : EmailTemplateContentCommand.of(key, template);
        return this;
    }

    public EmailSendMessageCreateCommandBuilder footer(String key, String template) {
        this.footer = (key == null && template == null) ?
                null : EmailTemplateContentCommand.of(key, template);
        return this;
    }

    private EmailTemplateContentCommand givenDefaultEmailBody() {
        String fileKey = "%s/%s.html".formatted(generateFileKey(UUID.randomUUID().toString()), "body");
        String template = "<div>body</div>";
        return EmailTemplateContentCommand.of(fileKey, template);
    }

    private String generateFileKey(String templateKey) {
        return "/template/%s".formatted(templateKey);
    }

    public EmailSendMessageCreateCommand build() {
        Map<EmailTemplateSectionEnum, EmailTemplateContentCommand> templateMap = new HashMap<>();

        if (header != null) templateMap.put(EmailTemplateSectionEnum.HEADER, header);
        if (body != null) templateMap.put(EmailTemplateSectionEnum.BODY, body);
        if (footer != null) templateMap.put(EmailTemplateSectionEnum.FOOTER, footer);

        return new EmailSendMessageCreateCommand(
                this.title,
                templateMap
        );
    }
}
