package com.ums.schedule.fixture.email;

import com.ums.schedule.application.ums.email.generator.model.RenderedTemplate;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplateContent;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.fixture.email.attachment.SecurityMailBuilder;

import java.util.ArrayList;
import java.util.List;

public class RenderedTemplateBuilder {
    private ConvertType convertType;
    private EmailType emailType;
    private SecurityMail securityMail;
    private RenderedTemplateContent header;
    private RenderedTemplateContent body;
    private RenderedTemplateContent footer;
    private RenderedTemplateContent cover;
    private List<RenderedTemplateContent> attachments = new ArrayList<>();

    public static RenderedTemplateBuilder builder() {
        return new RenderedTemplateBuilder();
    }

    public RenderedTemplateBuilder() {
        this.emailType = EmailType.SECURITY;
        this.convertType = ConvertType.HTML;
        this.securityMail = SecurityMailBuilder.builder().build();
        this.body = RenderedTemplateContentBuilder.builder().build();
        this.cover = RenderedTemplateContentBuilder.builder().build();
    }

    public RenderedTemplateBuilder emailType(EmailType emailType) {
        this.emailType = emailType;
        return this;
    }

    public RenderedTemplateBuilder securityMail(SecurityMail securityMail) {
        this.securityMail = securityMail;
        return this;
    }

    public RenderedTemplateBuilder convertType(ConvertType convertType) {
        this.convertType = convertType;
        return this;
    }

    public RenderedTemplateBuilder header(RenderedTemplateContent header) {
        this.header = header;
        return this;
    }

    public RenderedTemplateBuilder body(RenderedTemplateContent body) {
        this.body = body;
        return this;
    }

    public RenderedTemplateBuilder footer(RenderedTemplateContent footer) {
        this.footer = footer;
        return this;
    }

    public RenderedTemplateBuilder cover(RenderedTemplateContent cover) {
        this.cover = cover;
        return this;
    }

    public RenderedTemplateBuilder attachments(List<RenderedTemplateContent> attachments) {
        this.attachments = attachments;
        return this;
    }

    public RenderedTemplate build() {
        return new RenderedTemplate(
                convertType,
                emailType,
                securityMail,
                header,
                body,
                footer,
                cover,
                attachments
        );
    }
}
