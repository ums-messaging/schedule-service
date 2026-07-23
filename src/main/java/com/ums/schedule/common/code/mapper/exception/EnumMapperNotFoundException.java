package com.ums.schedule.common.code.mapper.exception;

import com.ums.schedule.common.code.api.EnumMapperErrorCode;
import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.exception.BusinessException;

public class EnumMapperNotFoundException extends BusinessException {
    protected EnumMapperNotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }


    public static EnumMapperNotFoundException of(EnumMapper key, String value) {
        return new EnumMapperNotFoundException(EnumMapperErrorCode.INVALID_CODE, key.code(), value);
    }

    public static EnumMapperNotFoundException of(EnumMapper key) {
        return new EnumMapperNotFoundException(EnumMapperErrorCode.INVALID_CODE, key.code());
    }
}
