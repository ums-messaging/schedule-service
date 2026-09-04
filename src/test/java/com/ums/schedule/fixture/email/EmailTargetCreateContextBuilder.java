package com.ums.schedule.fixture.email;

import com.ums.schedule.application.ums.common.target.result.SendTargetResult;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import freemarker.template.Template;

public class EmailTargetCreateContextBuilder {
    private TargetUploadReport targetUpload;
    private EmailSendMessage sendMessage;
    private SendTargetResult targetMessageResult;
    private String subject;
    private Template header;
    private Template footer;
    private String bodyTemplate;
    private String attachments;

    public static EmailTargetCreateContextBuilder builder() {
        return new EmailTargetCreateContextBuilder();
    }

    public EmailTargetCreateContextBuilder targetUpload(TargetUploadReport targetUpload) {
        this.targetUpload = targetUpload;
        return this;
    }

    public EmailTargetCreateContextBuilder sendMessage(EmailSendMessage sendMessage) {
        this.sendMessage = sendMessage;
        return this;
    }

    public EmailTargetCreateContextBuilder targetMessageResult(SendTargetResult targetMessageResult) {
        this.targetMessageResult =targetMessageResult;
        return this;
    }

    public EmailTargetCreateContextBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailTargetCreateContextBuilder header(Template header) {
        this.header = header;
        return this;
    }

    public EmailTargetCreateContextBuilder footer(Template footer) {
        this.footer = footer;
        return this;
    }

    public EmailTargetCreateContextBuilder bodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
        return this;
    }

    public EmailTargetCreateContextBuilder attachments(String attachments) {
        this.attachments = attachments;
        return this;
    }
}
