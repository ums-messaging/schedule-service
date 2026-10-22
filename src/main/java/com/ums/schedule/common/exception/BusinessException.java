package com.ums.schedule.common.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import lombok.Getter;

import java.text.MessageFormat;

@Getter
public abstract class BusinessException extends RuntimeException {
    private final Object id;
    private final ErrorCode errorCode;
    private final Object[] args;

    protected BusinessException(ErrorCode errorCode, Object... args) {
        this.id = null;
        this.errorCode = errorCode;
        this.args = args;
    }

    protected BusinessException(Object id, ErrorCode errorCode, Object... args) {
        this.id = id;
        this.errorCode = errorCode;
        this.args = args;
    }

    public String getErrorMessage() {
        String errorMessage = MessageFormat.format(errorCode.description(), args);
        if(errorMessage.length() > 1000) {
            return errorMessage.substring(0, 999);
        }
        return errorMessage;
    }
}
