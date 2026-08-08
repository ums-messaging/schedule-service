package com.ums.schedule.fixture.target_upload;

import com.ums.schedule.application.target.uploader.model.TargetUploadContext;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.domain.message.email.EmailSendMessage;

import java.util.UUID;

public class TargetUploadContextBuilder {
    private UUID uploadId;
    private EmailTemplate template;
    private EmailSendMessage sendMessage;

    public static TargetUploadContextBuilder builder() {
        return new TargetUploadContextBuilder();
    }

    private TargetUploadContextBuilder() { }

    public TargetUploadContextBuilder uploadId(UUID uuid) {
        this.uploadId = uuid;
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

    public TargetUploadContext build() {
        return new TargetUploadContext(
                uploadId,
                template,
                sendMessage
        );
    }
}
