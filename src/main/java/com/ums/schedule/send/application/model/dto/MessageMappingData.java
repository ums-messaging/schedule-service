package com.ums.schedule.send.application.model.dto;

import java.util.Map;

public record MessageMappingData<T> (
        String format,
        T value
) {

    public static MessageMappingData parse(String value) {
        return new MessageMappingData(
                "string",
                value
        );
    }
}
