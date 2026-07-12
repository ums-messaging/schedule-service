package com.ums.schedule.application.ums.email.convert;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.code.AttachmentType;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import org.flywaydb.core.internal.util.StringUtils;

import java.util.EnumMap;
import java.util.Map;

public record ConvertedAttachment(
        ConvertTypeEnum convertType,
        String fileKey,
        String fileKeyTemplate,
        Long fileSize,
        String attachmentName,
        String downloadName
) {
    public static ConvertedAttachment of(EnumMapperValue convertType, AttachmentContext context, String fileKeyTemplate) {
        String fullFileKeyTemplate = "%s.%s".formatted(fileKeyTemplate, convertType.description());
        return new ConvertedAttachment(
                ConvertTypeEnum.valueOf(convertType.code()),
                context.key(),
                fullFileKeyTemplate,
                context.fileSize(),
                context.attachmentName(),
                context.downloadName()
        );
    }

    public Map<AttachmentType, String> toKeyMap() {
        Map<AttachmentType, String> map = new EnumMap<>(AttachmentType.class);
        putToMap(map, AttachmentType.DIRECT, fileKey);
        putToMap(map, AttachmentType.TEMPLATE, fileKeyTemplate);
        return map;
    }

    private void putToMap(Map<AttachmentType, String> map, AttachmentType type, String fileKey) {
        if(StringUtils.hasText(fileKey)) {
           map.put(type, fileKey);
        }
    }
}
