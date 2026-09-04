package com.ums.schedule.common.exception;

import com.ums.schedule.common.code.api.ErrorCode;

public abstract class ValidationException extends BusinessException {
    protected ValidationException(String messagePrefix, ErrorCode errorCode) {
        super(messagePrefix, errorCode);
    }

}
