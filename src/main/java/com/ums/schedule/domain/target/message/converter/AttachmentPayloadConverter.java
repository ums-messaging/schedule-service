package com.ums.schedule.domain.target.message.converter;

import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.target.message.AttachmentPayload;
import jakarta.persistence.AttributeConverter;

import java.util.List;

public class AttachmentPayloadConverter implements AttributeConverter<List<AttachmentPayload>, String> {
    @Override
    public String convertToDatabaseColumn(List<AttachmentPayload> attribute) {
        return JsonUtil.toJson(attribute);
    }

    @Override
    public List<AttachmentPayload> convertToEntityAttribute(String dbData) {
        return JsonUtil.toList(dbData, AttachmentPayload.class);
    }
}
