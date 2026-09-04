package com.ums.schedule.common.converter.state;

import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public abstract class StatusStateConverter implements AttributeConverter<StatusState, String> {
    private final Class<? extends StatusStateType> enumClass;

    @Override
    public String convertToDatabaseColumn(StatusState attribute) {
        return attribute == null ? null : attribute.getCurrentCode().value();
    }

    @Override
    public StatusState convertToEntityAttribute(String dbData) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.value().equals(dbData))
                .findFirst()
                .map(e -> e.createStatus())
                .orElseThrow();
    }
}
