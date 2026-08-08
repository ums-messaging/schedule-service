package com.ums.schedule.application.ums.email.exception;

import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.exception.PolicyViolationException;

public class EmailPolicyViolationException extends PolicyViolationException {
    protected EmailPolicyViolationException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }


}
