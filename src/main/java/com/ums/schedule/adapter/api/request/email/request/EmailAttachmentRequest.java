package com.ums.schedule.adapter.api.request.email.request;

import com.ums.schedule.application.ums.email.template.query.model.EmailAttachmentDetailQuery;
import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import jakarta.validation.constraints.NotBlank;

import java.util.EnumMap;
import java.util.Map;

public record EmailAttachmentRequest(
        String type,
        String fileKey,
        @NotBlank(message = "EMAIL_SEND_REQUEST:ATTACHMENT_NAME_REQUIRED")
        String attachmentName,
        @NotBlank(message = "EMAIL_SEND_REQUEST:DOWNLOAD_NAME_REQUIRED")
        String downloadName
) {
    public static EmailAttachmentRequest of(EmailAttachment message) {
        return new EmailAttachmentRequest(
                "",
                message.getFileKey(),
                message.getAttachmentName(),
                message.getDownloadName()
        );
    }

    public EmailAttachmentDetailQuery toQuery(EnumMapperValue type) {
        return EmailAttachmentDetailQuery.toQuery(type, this);
    }
}
