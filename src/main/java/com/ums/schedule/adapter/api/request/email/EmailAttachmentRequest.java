package com.ums.schedule.adapter.api.request.email;

import com.ums.schedule.domain.message.email.attachment.EmailAttachment;

public record EmailAttachmentRequest(
        String fileKeySuffix,
        String fileKeySuffixTemplate,
        String attachmentName,
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
