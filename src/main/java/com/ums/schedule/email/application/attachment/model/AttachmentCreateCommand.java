package com.ums.schedule.email.application.attachment.model;

import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.email.adaptor.in.rest.message.request.AttachmentRequest;

public record AttachmentCreateCommand(
        AttachmentType type,
        String fileKey,
        String attachmentName,
        String downloadName,
        String contentType,
        Long fileSize
) {
    public static AttachmentCreateCommand of(
            AttachmentRequest request,
            AttachmentFindResult result
    ) {

        return new AttachmentCreateCommand(
                AttachmentType.valueOf(result.type().code()),
                result.fileKey(),
                request.attachmentName(),
                request.downloadName(),
                result.contentType(),
                result.fileSize()
        );
    }
}
