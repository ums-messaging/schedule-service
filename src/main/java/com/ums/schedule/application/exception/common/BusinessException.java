package com.ums.schedule.application.exception.common;

import com.ums.schedule.application.exception.ApplicationException;

public abstract class BusinessException extends ApplicationException {
    protected BusinessException(String message) {
        super(message);
    }
}
