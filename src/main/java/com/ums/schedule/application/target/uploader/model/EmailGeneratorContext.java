package com.ums.schedule.application.target.uploader.model;

import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;

import java.util.UUID;

public record EmailGeneratorContext(
        TargetUploadReport targetUploadReport,
        EmailSendMessage sendMessage,
        EmailTemplate template
) {
    public static EmailGeneratorContext of(TargetUploadReport targetUploadReport, EmailTemplate template, EmailSendMessage sendMessage) {
        return new EmailGeneratorContext(
                targetUploadReport,
                sendMessage,
                template
        );
    }
}
