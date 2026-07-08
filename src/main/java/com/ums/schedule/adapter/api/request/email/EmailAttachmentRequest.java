package com.ums.schedule.adapter.api.request.email;

import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.resource.email.policy.AttachmentPolicy;

public record EmailAttachmentRequest(
        String fileKeySuffix,
        String fileKeySuffixTemplate,
        String attachmentName,
        String downloadName
) {
    public static EmailAttachmentRequest of(EmailAttachment message) {
        AttachmentPolicy policy = message.getAttachmentPolicy();
        return new EmailAttachmentRequest(
                message.getFileKeyTemplate(),
                message.getFileKey(),
                policy.getAttachmentName(),
                policy.getDownloadName()
        );
    }
}
