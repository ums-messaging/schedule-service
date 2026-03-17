package com.ums.schedule.common.code;

public record EnumMapperValue(
        String code,
        String value,
        String description
) {
    public static EnumMapperValue fromEnumMapperType(EnumMapperType mapperType) {
        return new EnumMapperValue(mapperType.code(), mapperType.value(), mapperType.description());
    }
}
