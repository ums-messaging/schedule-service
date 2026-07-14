package com.ums.schedule.application.exception.common;

import com.ums.schedule.application.exception.ApplicationException;

public abstract class NotSupportedException extends ApplicationException {
    protected NotSupportedException(String message) {
        super(message);
    }

    protected NotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    protected NotSupportedException(Throwable cause) {
        super(cause);
    }
}
