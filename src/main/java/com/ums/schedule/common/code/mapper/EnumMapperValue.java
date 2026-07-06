package com.ums.schedule.common.code.mapper;

public record EnumMapperValue(
        String code,
        String value,
        String description
) {
    public static EnumMapperValue fromEnumMapperType(EnumMapperType mapperType) {
        if(mapperType == null) {
            return null;
        }
        return new EnumMapperValue(mapperType.code(), mapperType.value(), mapperType.description());
    }


}
