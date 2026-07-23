package com.ums.schedule.common.exception;

import com.ums.schedule.common.code.api.ErrorCode;

public abstract class PolicyViolationException extends BusinessException {
    protected PolicyViolationException(Object id, ErrorCode errorCode, Object... args) {
        super(id, errorCode, args);
    }
    protected PolicyViolationException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}