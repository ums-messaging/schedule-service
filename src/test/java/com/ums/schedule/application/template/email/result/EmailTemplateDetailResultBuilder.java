package com.ums.schedule.application.template.email.result;

import com.ums.schedule.application.sendrequest.message.email.result.EmailContentResult;
import com.ums.schedule.application.sendrequest.message.email.result.EmailTemplateDetailResult;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EmailTemplateDetailResultBuilder {
    private String emailContentId;
    private String msgTitle;
    private String imageDir;
    private List<EmailContentResult> contents;

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

    public EmailTemplateDetailResultBuilder contents(EmailContentResult... contents) {
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
