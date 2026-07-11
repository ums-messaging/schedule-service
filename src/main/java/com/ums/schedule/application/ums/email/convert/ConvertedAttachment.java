package com.ums.schedule.application.ums.email.convert;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.code.AttachmentType;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;

public record ConvertedAttachment(
        ConvertTypeEnum convertType,
        AttachmentType type,
        String key
) {
    public static ConvertedAttachment of(EnumMapperValue convertType, String fileKey) {
        return new ConvertedAttachment(
                ConvertTypeEnum.valueOf(convertType.code()),
                AttachmentType.DIRECT,
                fileKey
        );
    }

    public static ConvertedAttachment of(AttachmentType type, String key) {
        return new ConvertedAttachment(
                ConvertTypeEnum.NONE,
                type,
                key
        );
    }
}
