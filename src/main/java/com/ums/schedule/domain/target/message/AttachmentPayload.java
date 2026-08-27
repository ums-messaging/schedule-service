package com.ums.schedule.domain.target.message;

import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplateContent;
import com.ums.schedule.common.code.email.ConvertType;

public record AttachmentPayload(
        String fileKey,
        String attachmentName,
        String downloadName
) {

    public static AttachmentPayload of(EmailTemplateContent content, String fileKey) {
        return new AttachmentPayload(
                fileKey,
                content.attachmentName(),
                content.downloadName()
        );
    }

}
