package com.ums.schedule.domain.sendrequest.message.email;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmailSendMessageBuilder {
    private SendMessage sendMessage;
    private UUID id;

    private String subject;

    private String headerTemplateKey;
    private String headerTemplate;

    private String bodyTemplateKey;
    private String bodyTemplate;

    private String footerTemplateKey;
    private String footerTemplate;

    private String imageDir;
    private List<EmailAttachment> attachmentList = new ArrayList<>();

    public static EmailSendMessageBuilder builder() {
        return new EmailSendMessageBuilder();
    }

    private EmailSendMessageBuilder() {
        this.subject = "Email Message Subject";
        this.bodyTemplateKey = "body.html";
        this.bodyTemplate = "<h1>body</h1>";
    }

    public EmailSendMessageBuilder sendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
        return this;
    }

    public EmailSendMessageBuilder id(UUID uuid) {
        this.id = id;
        return this;
    }

    public EmailSendMessageBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailSendMessageBuilder headerTemplateKey(String headerTemplateKey) {
        this.headerTemplateKey = headerTemplateKey;
        return this;
    }

    public EmailSendMessageBuilder headerTemplate(String headerTemplate) {
        this.headerTemplate = headerTemplate;
        return this;
    }

    public EmailSendMessageBuilder bodyTemplateKey(String bodyTemplateKey) {
        this.bodyTemplateKey = bodyTemplateKey;
        return this;
    }

    public EmailSendMessageBuilder bodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
        return this;
    }

    public EmailSendMessageBuilder footerTemplateKey(String footerTemplateKey) {
        this.footerTemplateKey = footerTemplateKey;
        return this;
    }

    public EmailSendMessageBuilder footerTemplate(String footerTemplate) {
        this.footerTemplate = footerTemplate;
        return this;
    }

    public EmailSendMessage build() {
        return new EmailSendMessage(
                this.sendMessage,
                this.id,
                this.subject,
                this.headerTemplateKey,
                this.headerTemplate,
                this.bodyTemplateKey,
                this.bodyTemplate,
                this.footerTemplateKey,
                this.footerTemplate,
                this.imageDir,
                this.attachmentList
        );
    }
}
