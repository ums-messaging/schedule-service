package com.ums.schedule.application.ums.common.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.exception.DbNotFoundException;

public class TemplateNotFoundException extends DbNotFoundException {
    protected TemplateNotFoundException(Object id, ErrorCode errorCode, Object... args) {
        super(id, errorCode, args);
    }
}
