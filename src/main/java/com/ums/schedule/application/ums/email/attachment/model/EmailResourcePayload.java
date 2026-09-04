package com.ums.schedule.application.ums.email.attachment.model;


public record EmailResourcePayload(
        Long attachmentId,
        String fileKey,
        String attachmentName,
        String downloadName
) {
}
