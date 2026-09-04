package com.ums.schedule.fixture.entity;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;

import java.util.UUID;

public class EmailTargetMessageEntityBuilder {
    private String email;
    private String subject;
    private String headerMessage;
    private String bodyMessage;
    private String footerMessage;
    private String attachments;

    private EmailSendMessage message;

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

    public TargetMessage build() {
        EmailTargetMessage message = new EmailTargetMessage(
                email,
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
