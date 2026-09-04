package com.ums.schedule.application.ums.email.attachment.model;

import com.ums.schedule.application.ums.email.template.query.model.EmailAttachmentDetailQuery;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.common.code.email.AttachmentType;
import org.springframework.util.StringUtils;

import java.util.Optional;

public record AttachmentContext(
        AttachmentType type,
        String key,
        String attachmentName,
        String downloadName,
        Long fileSize
) {
    public static AttachmentContext of(EmailTemplateContentResult content) {
        return Optional.ofNullable(content)
                .map(c -> {
                    if(StringUtils.hasText(c.fileKey())) {
                        return AttachmentContext.of(AttachmentType.DIRECT, content);
                    }
                    return AttachmentContext.of(AttachmentType.TEMPLATE, content);
                }).orElse(null);
    }

    public static AttachmentContext of(AttachmentType type,  EmailTemplateContentResult content) {
        if(content == null) {
            return null;
        }
        String fileKey = (AttachmentType.DIRECT == type) ? content.fileKey() : content.fileKeyTemplate();
        return new AttachmentContext(
                type,
                fileKey,
                content.attachmentName(),
                content.downloadName(),
                content.fileSize()
        );
    }

    public static AttachmentContext of(EmailAttachmentDetailQuery query, String fileKey) {
        return new AttachmentContext(
                query.type(),
                fileKey,
                query.attachmentName(),
                query.downloadName(),
                null);
    }
}
