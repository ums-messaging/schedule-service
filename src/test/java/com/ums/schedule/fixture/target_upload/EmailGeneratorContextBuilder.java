package com.ums.schedule.fixture.target_upload;

import com.ums.schedule.application.target.uploader.model.EmailGeneratorContext;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;

public class EmailGeneratorContextBuilder {
    private TargetUploadReport targetUploadReport;
    private EmailTemplate template;
    private EmailSendMessage sendMessage;

    public static EmailGeneratorContextBuilder builder() {
        return new EmailGeneratorContextBuilder();
    }

    private EmailGeneratorContextBuilder() { }

    public EmailGeneratorContextBuilder targetUploadReport(TargetUploadReport targetUploadReport) {
        this.targetUploadReport = targetUploadReport;
        return this;
    }

    public EmailGeneratorContextBuilder template(EmailTemplate template) {
        this.template = template;
        return this;
    }

    public EmailGeneratorContextBuilder emailSendMessage(EmailSendMessage emailSendMessage) {
        this.sendMessage = emailSendMessage;
        return this;
    }

    public EmailGeneratorContext build() {
        return new EmailGeneratorContext(
                targetUploadReport,
                sendMessage,
                template
        );
    }
}
