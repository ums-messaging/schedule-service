package com.ums.schedule.application.ums.email.message.model;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentCreateResult;
import com.ums.schedule.domain.message.email.EmailSendMessage;

import java.util.List;

public record EmailMessageCreateResult(
        String messageId,
        List<AttachmentCreateResult> attachments
) {
    public static EmailMessageCreateResult of(EmailSendMessage message, List<AttachmentCreateResult> attachments) {
        return new EmailMessageCreateResult(
                message.getId().toString(),
                attachments
        );
    }
}
