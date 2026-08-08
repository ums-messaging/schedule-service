package com.ums.schedule.application.ums.email.template.query.model;

import com.ums.schedule.adapter.api.request.email.request.EmailAttachmentRequest;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;


public record EmailAttachmentDetailQuery(
        AttachmentType type,
        String fileKey,
        String attachmentName,
        String downloadName
) {

    public static EmailAttachmentDetailQuery toQuery(EnumMapperValue type, EmailAttachmentRequest request) {
        return new EmailAttachmentDetailQuery(
                AttachmentType.valueOf(type.code()),
                request.fileKey(),
                request.attachmentName(),
                request.downloadName()
        );
    }

    public AttachmentContext toContext(String fileKey) {
        return AttachmentContext.of(this, fileKey);
    }
}
