package com.ums.schedule.domain.sendrequest.target.upload.converter;

import com.ums.schedule.common.util.JsonUtil;
import jakarta.persistence.AttributeConverter;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;

public class TargetMessageParamConverter implements AttributeConverter<Map<String, Object>, String> {
    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        if(attribute.isEmpty()) {
            return "";
        }
        return JsonUtil.toJson(attribute);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        if(!StringUtils.hasText(dbData)) {
            return Collections.emptyMap();
        }
        return JsonUtil.toMap(dbData, Object.class);
    }
}
