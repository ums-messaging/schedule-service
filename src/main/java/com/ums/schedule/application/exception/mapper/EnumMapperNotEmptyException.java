package com.ums.schedule.application.exception.mapper;

import com.ums.schedule.common.code.mapper.EnumMapper;

public class EnumMapperNotEmptyException extends EnumMapperException {
    public EnumMapperNotEmptyException(String message) {
        super(message);
    }

    public static EnumMapperNotEmptyException of(EnumMapper enumMapper) {
        return new EnumMapperNotEmptyException(String.format("%s의 코드 값은 필수 값입니다.", enumMapper.key()));
    }
}
