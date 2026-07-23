package com.ums.schedule.common.exception;

import com.ums.schedule.common.code.api.ErrorCode;

public class InternalServerException extends RuntimeException {
    protected InternalServerException(ErrorCode errorCode, Throwable e) {
        super(errorCode.description(), e);
    }
}
