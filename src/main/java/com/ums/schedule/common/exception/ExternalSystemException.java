package com.ums.schedule.common.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.mapper.EnumMapperType;
import lombok.Getter;

@Getter
public abstract class ExternalSystemException extends RuntimeException {
    private final String key;
    private final ErrorCode errorCode;
    private final Object[] args;

    protected ExternalSystemException(ErrorCode errorCode, Object... args) {
        this.key = null;
        this.errorCode = errorCode;
        this.args = args;
    }
    protected ExternalSystemException(String key, ErrorCode errorCode, Object... args) {
        this.key = key;
        this.errorCode = errorCode;
        this.args = args;
    }
}
