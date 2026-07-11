package com.ums.schedule.application.message.email.model;

import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.code.SecurityMailEnumMapper;

import java.util.Map;

public record AttachmentCreateCommand(
        EnumMapperValue convertType,
        String attachmentName,
        String downloadName,
        String fileKeyTemplate,
        String fileKey,
        Long fileSize,
        Map<SecurityMailEnumMapper, EnumMapperValue> securityPolicyMap,
        String passwordHash,
        String passwordFormat
) {
   

    public static AttachmentCreateCommand of(EmailTemplateContentResult content) {
        return new AttachmentCreateCommand(
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
