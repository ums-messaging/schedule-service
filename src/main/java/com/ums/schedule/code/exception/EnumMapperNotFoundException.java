package com.ums.schedule.code.exception;

import com.ums.schedule.code.EnumMapper;

public class EnumMapperNotFoundException extends RuntimeException {
    private EnumMapperNotFoundException(String message) {
        super(message);
    }
    public static EnumMapperNotFoundException forEnumMapperValue(EnumMapper enumMapperName, String value) {
        return new EnumMapperNotFoundException(
                String.format("%s 코드에서 %s 값을 찾을 수 없습니다. ",  enumMapperName.key(), value));
    }
    public static EnumMapperNotFoundException forEnumMapper(EnumMapper enumMapperName) {
        return new EnumMapperNotFoundException(
                String.format("%s는 등록되지 않은 코드입니다. ",  enumMapperName.key()));
    }
}
