package com.ums.schedule.template.domain.email;

import com.ums.schedule.attachment.code.StorageTypeEnum;
import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.attachment.domain.AttachmentPolicy;
import com.ums.schedule.attachment.domain.FileMetaData;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.domain.target.EmailSendTarget;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;

import java.util.Map;

public record EmailAttachment(
        StorageTypeEnum storageType,
        String contentType,
        String attachmentName,
        String downloadName,
        String fileKey
) {

    public static EmailAttachment of(EnumMapperValue storageType, EmailContentResponse response) {
        return new EmailAttachment(
                StorageTypeEnum.valueOf(storageType.code()),
                response.contentType(),
                response.attachmentName(),
                response.downloadName(),
                response.fileKey());
    }
}
