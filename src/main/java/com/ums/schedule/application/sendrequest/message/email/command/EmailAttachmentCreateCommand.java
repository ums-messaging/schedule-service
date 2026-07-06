package com.ums.schedule.application.sendrequest.message.email.command;

import com.ums.schedule.application.sendrequest.message.email.result.EmailContentResult;
import com.ums.schedule.domain.sendrequest.resource.email.code.AttachmentEnumMapper;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

import java.util.Map;

public record EmailAttachmentCreateCommand(
        EnumMapperValue convertType,
        String attachmentName,
        String downloadName,
        String fileKeyTemplate,
        String fileKey,
        Long fileSize,
        Map<AttachmentEnumMapper, EnumMapperValue> securityPolicyMap,
        String passwordHash,
        String passwordFormat
) {
    public static EmailAttachmentCreateCommand bodyOf(EnumMapperValue convertType, EmailContentResult body, String fileKeyTemplate, SecurityPolicyCommand securityPolicy) {
        if(securityPolicy == null) {
            return EmailAttachmentCreateCommand.of(convertType, body, fileKeyTemplate);
        }
        return new EmailAttachmentCreateCommand(
                convertType,
                body.attachmentName(),
                body.downloadName(),
                fileKeyTemplate,
                body.fileKey(),
                body.fileSize(),
                securityPolicy.securityMap(),
                securityPolicy.passwordHash(),
                securityPolicy.passwordFormat()
        );
    }

    private static EmailAttachmentCreateCommand of(EnumMapperValue convertType, EmailContentResult body, String fileKeyTemplate) {
        return new EmailAttachmentCreateCommand(
                convertType,
                body.attachmentName(),
                body.downloadName(),
                fileKeyTemplate,
                body.fileKey(),
                body.fileSize(),
                null, null, null
        );
    }

    public static EmailAttachmentCreateCommand of(EmailContentResult content) {
        return new EmailAttachmentCreateCommand(
            EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE),
            content.attachmentName(),
            content.downloadName(),
            content.fileKeyTemplate(),
            content.fileKey(),
            content.fileSize(),
            null,
            null,
                null
        );
    }
}
