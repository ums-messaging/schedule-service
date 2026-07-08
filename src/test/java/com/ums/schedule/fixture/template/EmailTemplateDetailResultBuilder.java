package com.ums.schedule.fixture.template;

import com.ums.schedule.application.template.email.query.model.EmailTemplateContentResult;
import com.ums.schedule.application.template.email.query.model.EmailTemplateDetailResult;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EmailTemplateDetailResultBuilder {
    private String emailContentId;
    private String msgTitle;
    private String imageDir;
    private List<EmailTemplateContentResult> contents;

    public static EmailTemplateDetailResultBuilder builder() {
        return new EmailTemplateDetailResultBuilder();
    }

    private EmailTemplateDetailResultBuilder() {
        this.emailContentId = UUID.randomUUID().toString();
        this.msgTitle = "Email Message Subject";
        this.imageDir = "/images";
    }

    public EmailTemplateDetailResultBuilder title(String title) {
        this.msgTitle = title;
        return this;
    }

    public EmailTemplateDetailResultBuilder contents(EmailTemplateContentResult... contents) {
        this.contents = Arrays.stream(contents)
                .toList();
        return this;
    }

    public EmailTemplateDetailResult build() {
        return new EmailTemplateDetailResult(
                this.emailContentId,
                this.msgTitle,
                this.imageDir,
                this.contents
        );
    }
}
