package com.ums.schedule.adapter.api.request.email.request;

import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import jakarta.validation.constraints.NotBlank;

public record EmailAttachmentRequest(
        String fileKeySuffix,
        String fileKeySuffixTemplate,
        @NotBlank(message = "EMAIL_SEND_REQUEST:ATTACHMENT_NAME_REQUIRED")
        String attachmentName,
        @NotBlank(message = "EMAIL_SEND_REQUEST:DOWNLOAD_NAME_REQUIRED")
        String downloadName
) {
    public static EmailAttachmentRequest of(EmailAttachment message) {
        return new EmailAttachmentRequest(
                message.getFileKeyTemplate(),
                message.getFileKey(),
                message.getAttachmentName(),
                message.getDownloadName()
        );
    }
}
