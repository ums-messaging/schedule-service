package com.ums.schedule.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T toObject(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            return Collections.emptyList();
        }

        try {
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return mapper.readValue(json, listType);
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    public static <T> Map<String, T> toMap(String json, Class<T> clazz) {
        if(!StringUtils.hasText(json)) {
            return Collections.emptyMap();
        }

        MapType mapType = mapper.getTypeFactory().constructMapType(Map.class, String.class, clazz);

        try {
            return mapper.readValue(json, mapType);
        } catch (JsonProcessingException e) {
            return Collections.emptyMap();
        }
    }

    public static String toJson(Object object) {
        if(object == null) {
            return "";
        }

        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
