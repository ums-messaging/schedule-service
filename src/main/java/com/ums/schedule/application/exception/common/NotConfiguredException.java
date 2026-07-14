package com.ums.schedule.application.exception.common;

import com.ums.schedule.application.exception.ApplicationException;

public abstract class NotConfiguredException extends ApplicationException {

    protected NotConfiguredException(String message) {
        super(message);
    }
}
