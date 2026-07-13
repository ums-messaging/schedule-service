package com.ums.schedule.fixture.email.message;

import com.ums.schedule.application.ums.common.template.TemplateResult;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.message.provider.EmailMessageContext;
import com.ums.schedule.application.ums.email.security.SecurityMail;

import java.util.Arrays;
import java.util.List;


public class EmailMessageContextBuilder {
    private TemplateResult template;
    private String title;
    private String headerKey;
    private String bodyKey;
    private String footerKey;
    private String imageDir;
    private SecurityMail securityMail;
    private List<ConvertedAttachment> attachments;

    public static EmailMessageContextBuilder builder() {
        return new EmailMessageContextBuilder();
    }
    private EmailMessageContextBuilder() {
        this.title = "hello world!";
        this.headerKey = "header.html";
        this.bodyKey = "body.html";
        this.footerKey = "footer.html";
    }

    public EmailMessageContextBuilder template(TemplateResult template) {
        this.template = template;
        return this;
    }

    public EmailMessageContextBuilder title(String title) {
        this.title = title;
        return this;
    }

    public EmailMessageContextBuilder headerKey(String headerKey) {
        this.headerKey = headerKey;
        return this;
    }

    public EmailMessageContextBuilder bodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
        return this;
    }

    public EmailMessageContextBuilder footerKey(String footerKey) {
        this.footerKey = footerKey;
        return this;
    }

    public EmailMessageContextBuilder attachmentList(ConvertedAttachment... attachments) {
        if(attachments == null) {
            this.attachments = List.of();
            return this;
        }
        this.attachments = Arrays.asList(attachments);
        return this;
    }


    public EmailMessageContext build() {
        return new EmailMessageContext(
                this.template,
                this.title,
                this.headerKey,
                this.bodyKey,
                this.footerKey,
                this.imageDir,
                this.securityMail,
                this.attachments
        );
    }
}
