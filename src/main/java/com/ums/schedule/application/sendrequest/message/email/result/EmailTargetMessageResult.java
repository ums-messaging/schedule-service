package com.ums.schedule.application.sendrequest.message.email.result;

import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.resource.email.policy.AttachmentPolicy;
import com.ums.schedule.domain.sendrequest.target.SendTarget;

public record EmailTargetMessageResult(
        String objectKey,
        String attachmentName,
        String downloadName
) {
    public static EmailTargetMessageResult of(String fileKey, AttachmentPolicy policy, SendTarget target) {
        String objectKey = target.parse(fileKey);
        String attachmentName = target.parse(policy.getAttachmentName());
        String downloadName = target.parse(policy.getDownloadName());
        return new EmailTargetMessageResult(objectKey, attachmentName, downloadName);
    }

    public static EmailTargetMessageResult of(EmailAttachment message, SendTarget target) {
        AttachmentPolicy policy = message.getAttachmentPolicy();
        String objectKey = target.parse(message.getFileKey());
        String attachmentName = target.parse(policy.getAttachmentName());
        String downloadName = target.parse(policy.getDownloadName());
        return new EmailTargetMessageResult(objectKey, attachmentName, downloadName);
    }
}
