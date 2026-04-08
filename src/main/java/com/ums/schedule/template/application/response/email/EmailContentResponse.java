package com.ums.schedule.template.application.response.email;


import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.common.code.EnumMapperValue;

import static com.ums.schedule.template.domain.code.ConvertTypeEnum.NONE;

public record EmailContentResponse(
        String section,
        String format,
        String content,
        String contentType,
        String attachmentName,
        String downloadName,
        String storageType,
        String baseDir,
        String fileKey,
        String fileUrl,
        String originalFileName,
        Long fileSize
) {
    public Attachment toAttachment() {
        Attachment attachment = Attachment.of(EnumMapperValue.fromEnumMapperType(NONE));
        attachment.defineAttachmentPolicy(attachmentName, downloadName);
        attachment.defineFileMetadata(this);
        return attachment;
    }
}
