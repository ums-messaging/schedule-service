package com.ums.schedule.fixture.email;

import com.ums.schedule.application.ums.email.generator.model.EmailTargetMessageCreateContext;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplateContent;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import freemarker.template.Template;

import java.util.List;

public class EmailTargetMessageCreateContextBuilder {
    private EmailSendMessage sendMessage;
    private String subject;
    private Template header;
    private String bodyTemplate;
    private Template footer;
    private List<RenderedTemplateContent> attachmentList;

    public static EmailTargetMessageCreateContextBuilder builder() {
        return new EmailTargetMessageCreateContextBuilder();
    }

    private EmailTargetMessageCreateContextBuilder() {
    }

    public EmailTargetMessageCreateContextBuilder sendMessage(EmailSendMessage sendMessage) {
        this.sendMessage = sendMessage;
        return this;
    }

    public EmailTargetMessageCreateContextBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailTargetMessageCreateContextBuilder header(Template header) {
        this.header = header;
        return this;
    }

    public EmailTargetMessageCreateContextBuilder bodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
        return this;
    }

    public EmailTargetMessageCreateContextBuilder footer(Template footer) {
        this.footer = footer;
        return this;
    }

    public EmailTargetMessageCreateContextBuilder attachmentList(List<RenderedTemplateContent> attachmentList) {
        this.attachmentList = attachmentList;
        return this;
    }

    public EmailTargetMessageCreateContext build() {
        return new EmailTargetMessageCreateContext(
                subject,
                header,
                bodyTemplate,
                footer,
                sendMessage,
                attachmentList
        );
    }
}
