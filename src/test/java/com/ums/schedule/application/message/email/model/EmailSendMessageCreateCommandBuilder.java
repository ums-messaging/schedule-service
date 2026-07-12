package com.ums.schedule.application.message.email.model;

import com.ums.schedule.domain.sendrequest.message.SendMessage;


public class EmailSendMessageCreateCommandBuilder {
    private SendMessage sendMessage;
    private String title;
    private String headerKey;
    private String bodyKey;
    private String footerKey;

    public static EmailSendMessageCreateCommandBuilder builder() {
        return new EmailSendMessageCreateCommandBuilder();
    }
    private EmailSendMessageCreateCommandBuilder() {
        this.headerKey = "header.html";
        this.bodyKey = "body.html";
        this.footerKey = "footer.html";
    }

    public EmailSendMessageCreateCommandBuilder title(String title) {
        this.title = title;
        return this;
    }

    public EmailSendMessageCreateCommandBuilder headerKey(String headerKey) {
        this.headerKey = headerKey;
        return this;
    }

    public EmailSendMessageCreateCommandBuilder bodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
        return this;
    }

    public EmailSendMessageCreateCommandBuilder footerKey(String footerKey) {
        this.footerKey = footerKey;
        return this;
    }

    public EmailSendMessageCreateCommandBuilder sendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
        return this;
    }

    public EmailSendMessageCreateCommand build() {

        return new EmailSendMessageCreateCommand(
                this.sendMessage,
                this.title,
                this.headerKey,
                this.bodyKey,
                this.footerKey
        );
    }
}
