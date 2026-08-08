package com.ums.schedule.domain.request.message.email;

import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.message.email.convert.ConvertMail;
import com.ums.schedule.domain.message.email.security.SecurityMailPolicy;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import com.ums.schedule.fixture.email.convert.ConvertMailBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmailSendMessageBuilder {
    private SendMessage sendMessage;
    private UUID id;
    private EmailType emailType;

    private String subject;

    private String headerTemplateKey;
    private String bodyTemplateKey;
    private String coverTemplateKey;
    private String footerTemplateKey;

    private String imageDir;
    private List<EmailAttachment> attachmentList = new ArrayList<>();

    private SecurityMailPolicy securityMailPolicy;
    private ConvertMail convertMail;
    private EmailTargetMessage targetMessage;

    public static EmailSendMessageBuilder builder() {
        return new EmailSendMessageBuilder();
    }

    private EmailSendMessageBuilder() {
        this.emailType = EmailType.PLAIN;
        this.subject = "Email Message Subject";
        this.bodyTemplateKey = "body.html";
        this.convertMail = ConvertMailBuilder.builder().build();
    }

    public EmailSendMessageBuilder sendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
        return this;
    }

    public EmailSendMessageBuilder id(UUID uuid) {
        this.id = id;
        return this;
    }

    public EmailSendMessageBuilder emailType(EmailType emailType) {
        this.emailType = emailType;
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


    public EmailSendMessageBuilder bodyTemplateKey(String bodyTemplateKey) {
        this.bodyTemplateKey = bodyTemplateKey;
        return this;
    }

    public EmailSendMessageBuilder coverTemplateKey(String coverTemplateKey) {
        this.coverTemplateKey = coverTemplateKey;
        return this;
    }

    public EmailSendMessageBuilder footerTemplateKey(String footerTemplateKey) {
        this.footerTemplateKey = footerTemplateKey;
        return this;
    }

    public EmailSendMessageBuilder securityMailPolicy(SecurityMailPolicy securityMailPolicy) {
        this.securityMailPolicy = securityMailPolicy;
        return this;
    }

    public EmailSendMessageBuilder attachments(List<EmailAttachment> attachments) {
        this.attachmentList =attachments;
        return this;
    }

    public EmailSendMessageBuilder convertMail(ConvertMail convertMail) {
        this.convertMail = convertMail;
        return this;
    }

    public EmailSendMessageBuilder targetMessage(EmailTargetMessage targetMessage) {
        this.targetMessage = targetMessage;
        return this;
    }

    public EmailSendMessage build() {
        return new EmailSendMessage(
                this.sendMessage,
                this.id,
                this.emailType,
                this.subject,
                this.headerTemplateKey,
                this.bodyTemplateKey,
                this.coverTemplateKey,
                this.footerTemplateKey,
                this.imageDir,
                this.attachmentList,
                securityMailPolicy,
                convertMail
        );
    }
}
