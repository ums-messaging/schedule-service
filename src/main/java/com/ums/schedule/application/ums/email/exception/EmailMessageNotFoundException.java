package com.ums.schedule.application.ums.email.exception;

import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.exception.DbNotFoundException;

public class EmailMessageNotFoundException extends DbNotFoundException {
    protected EmailMessageNotFoundException(String id, ErrorCode errorCode, Object... args) {
        super(id, errorCode, args);
    }

    public static EmailMessageNotFoundException of(String id) {
        return new EmailMessageNotFoundException(id, EmailMessageErrorCode.NOT_FOUND_MESSAGE);
    }
}
