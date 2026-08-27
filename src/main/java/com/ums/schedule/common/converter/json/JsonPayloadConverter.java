package com.ums.schedule.common.converter.json;

import com.ums.schedule.common.util.JsonUtil;
import jakarta.persistence.AttributeConverter;

public abstract class JsonPayloadConverter implements AttributeConverter<JsonPayload, String> {
    public String convertToDatabaseColumn(JsonPayload attribute) {
        return attribute != null ? JsonUtil.toJson(attribute) : "";
    }

}
