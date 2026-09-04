package com.ums.schedule.fixture.email;

import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplateContent;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;

import java.util.ArrayList;
import java.util.List;

public class EmailTemplateBuilder {
    private String templateKey;
    private EmailType emailType;
    private ConvertType convertType;

    private String title;
    private SecurityMail securityMail;
    private EmailTemplateContent header;
    private EmailTemplateContent cover;
    private EmailTemplateContent body;
    private EmailTemplateContent footer;

    private List<EmailTemplateContent> attachments = new ArrayList<>();

    public static EmailTemplateBuilder builder() {
        return new EmailTemplateBuilder();
    }

    private EmailTemplateBuilder() {
        this.templateKey = "template_key";
        this.title = "${month}월 청구서";
    }

    public EmailTemplateBuilder emailType(EmailType emailType) {
        this.emailType = emailType;
        return this;
    }

    public EmailTemplateBuilder convertType(ConvertType convertType) {
        this.convertType = convertType;
        return this;
    }

    public EmailTemplateBuilder securityMail(SecurityMail securityMail) {
        this.securityMail = securityMail;
        return this;
    }

    public EmailTemplateBuilder header(EmailTemplateContent header) {
        this.header = header;
        return this;
    }

    public EmailTemplateBuilder body(EmailTemplateContent body) {
        this.body = body;
        return this;
    }

    public EmailTemplateBuilder cover(EmailTemplateContent cover) {
        this.cover = cover;
        return this;
    }

    public EmailTemplateBuilder footer(EmailTemplateContent footer) {
        this.footer = footer;
        return this;
    }

    public EmailTemplateBuilder attachmentList(List<EmailTemplateContent> attachments) {
        this.attachments = attachments;
        return this;
    }

    public EmailTemplateBuilder title(String title) {
        this.title = title;
        return this;
    }

    public EmailTemplate build() {
        return new EmailTemplate(
                templateKey,
                emailType,
                convertType,
                title,
                securityMail,
                header,
                cover,
                body,
                footer,
                attachments
        );
    }
}
