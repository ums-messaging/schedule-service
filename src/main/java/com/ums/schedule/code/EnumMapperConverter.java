package com.ums.schedule.code;

import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public abstract class EnumMapperConverter implements AttributeConverter<EnumMapperType , String> {
    private final Class<? extends EnumMapperType> enumClass;

    @Override
    public String convertToDatabaseColumn(EnumMapperType attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public EnumMapperType convertToEntityAttribute(String dbData) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.value().equals(dbData))
                .findFirst()
                .orElseThrow();
    }
}
