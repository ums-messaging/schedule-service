package com.ums.schedule.fixture.entity;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;

import java.util.UUID;

public class EmailTargetMessageEntityBuilder {
    private TargetMessage targetMessage;
    private UUID id;
    private String subject;
    private String headerMessage;
    private String bodyMessage;
    private String footerMessage;
    private String attachments;

    private EmailSendMessage message;
    private SendTarget sendTarget;

    public static EmailTargetMessageEntityBuilder builder() {
        return new EmailTargetMessageEntityBuilder();
    }

    private EmailTargetMessageEntityBuilder() {
        this.subject = "메일 제목";
        this.bodyMessage = "이메일 내용";
    }

    public EmailTargetMessageEntityBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailTargetMessageEntityBuilder headerMessage(String headerMessage) {
        this.headerMessage = headerMessage;
        return this;
    }

    public EmailTargetMessageEntityBuilder bodyMessage(String bodyMessage) {
        this.bodyMessage = bodyMessage;
        return this;
    }

    public EmailTargetMessageEntityBuilder footerMessage(String footerMessage) {
        this.footerMessage = footerMessage;
        return this;
    }

    public EmailTargetMessageEntityBuilder attachments(String attachments) {
        this.attachments = attachments;
        return this;
    }

    public EmailTargetMessageEntityBuilder sendMessage(EmailSendMessage sendMessage) {
        this.message = sendMessage;
        return this;
    }

    public EmailTargetMessageEntityBuilder targetMessage(TargetMessage targetMessage) {
        this.targetMessage = targetMessage;
        return this;
    }

    public EmailTargetMessage build() {
        EmailTargetMessage message = new EmailTargetMessage(
                id,
                subject,
                headerMessage,
                bodyMessage,
                footerMessage,
                attachments,
                this.message
        );
        return message;
    }
}
