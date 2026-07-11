package com.ums.schedule.application.message.email.model;

import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.message.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.target.SendTarget;

import java.util.Map;

public record AttachmentPipelineCommand(
        ConvertTypeEnum convertType,
        String filePrefix,
        String fileSuffix,
        String fileKey,
        String objectKey,
        String attachmentName,
        String downloadName,
        Map<String, Object> targetDataParam,
        String userPassword,
        int encryptionLength,
        boolean canModify,
        boolean canPrint
) {
    public static AttachmentPipelineCommand of(EmailAttachment attachment, SendTarget target) {
        return new AttachmentPipelineCommand(
                attachment.getConvertType(),
                target.getTargetKey(),
                attachment.getConvertType().value().toLowerCase(),
                attachment.getFileKey(),
                attachment.resolveTemplateFileKey(target),
                attachment.resolveAttachmentName(target),
                attachment.resolveDownloadName(target),
                target.getDataParam(),
                attachment.resolveSecurityPassword(target),
                attachment.getEncryptionLength(),
                attachment.getCanModify(),
                attachment.getCanPrint()
        );
    }
}
