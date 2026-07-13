package com.ums.schedule.fixture.template;

import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailResult;

import java.util.*;

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
        this.contents = new ArrayList<>();
    }

    public EmailTemplateDetailResultBuilder title(String title) {
        this.msgTitle = title;
        return this;
    }

    public EmailTemplateDetailResultBuilder contents(EmailTemplateContentResult... contents) {
        this.contents = Arrays.stream(
                            Optional.ofNullable(contents)
                                .orElse(List.of().toArray(EmailTemplateContentResult[]::new)))
                .filter(Objects::nonNull)
                .toList();
        return this;
    }

    public EmailTemplateDetailResultBuilder imageDir(String imageDir) {
        this.imageDir = imageDir;
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
