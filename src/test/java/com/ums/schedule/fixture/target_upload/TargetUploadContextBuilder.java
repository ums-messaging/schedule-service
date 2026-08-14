package com.ums.schedule.fixture.target_upload;

import com.ums.schedule.application.target.uploader.model.EmailGeneratorContext;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;

import java.util.UUID;

public class TargetUploadContextBuilder {
    private TargetUploadReport targetUploadReport;
    private EmailTemplate template;
    private EmailSendMessage sendMessage;

    public static TargetUploadContextBuilder builder() {
        return new TargetUploadContextBuilder();
    }

    private TargetUploadContextBuilder() { }

    public TargetUploadContextBuilder targetUploadReport(TargetUploadReport targetUploadReport) {
        this.targetUploadReport = targetUploadReport;
        return this;
    }

    public TargetUploadContextBuilder template(EmailTemplate template) {
        this.template = template;
        return this;
    }

    public TargetUploadContextBuilder emailSendMessage(EmailSendMessage emailSendMessage) {
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
