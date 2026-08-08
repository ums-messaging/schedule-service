package com.ums.schedule.application.target.uploader.model;

import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.domain.message.email.EmailSendMessage;

import java.util.UUID;

public record TargetUploadContext(
        UUID uploadId,
        EmailTemplate template,
        EmailSendMessage sendMessage
) {
    public static TargetUploadContext of(UUID reportId, EmailTemplate template, EmailSendMessage sendMessage) {
        return new TargetUploadContext(
                reportId,
                template,
                sendMessage
        );
    }

}
