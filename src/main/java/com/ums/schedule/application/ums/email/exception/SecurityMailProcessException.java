package com.ums.schedule.application.ums.email.exception;

import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.api.ErrorCode;

public class SecurityMailProcessException extends EmailPolicyViolationException {
    protected SecurityMailProcessException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static SecurityMailProcessException of() {
        return new SecurityMailProcessException(EmailMessageErrorCode.NOT_FOUND_CONTENT);
    }
}
