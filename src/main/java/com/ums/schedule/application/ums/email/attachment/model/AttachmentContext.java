package com.ums.schedule.application.ums.email.attachment.model;

import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.domain.message.email.code.AttachmentType;
import org.springframework.util.StringUtils;

public record AttachmentContext(
        AttachmentType type,
        String key,
        String attachmentName,
        String downloadName,
        Long fileSize
) {
    public static AttachmentContext of(EmailTemplateContentResult content) {
        if(StringUtils.hasText(content.fileKey())) {
            return AttachmentContext.of(AttachmentType.DIRECT, content);
        }
        return AttachmentContext.of(AttachmentType.TEMPLATE, content);
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

}
