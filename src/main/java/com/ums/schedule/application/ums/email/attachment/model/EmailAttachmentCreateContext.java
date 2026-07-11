package com.ums.schedule.application.ums.email.attachment.model;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.domain.message.exception.EmailSendMessage;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;

public record EmailAttachmentCreateContext(
        EmailSendMessage sendMessage,
        SecurityMailPolicy securityMail,
        ConvertedAttachment convertedAttachment,
        String attachmentName,
        String downloadName,
        String fileKey,
        Long fileSize
        ) {

        public static EmailAttachmentCreateContext of(EmailSendMessage message,
                                                      SecurityMailPolicy securityPolicy,
                                                      ConvertedAttachment attachment,
                                                      EmailTemplateContentResult content) {
                return new EmailAttachmentCreateContext(
                        message,
                        securityPolicy,
                        attachment,
                        content.attachmentName(),
                        content.downloadName(),
                        content.fileKey(),
                        content.fileSize()
                );
        }
}
