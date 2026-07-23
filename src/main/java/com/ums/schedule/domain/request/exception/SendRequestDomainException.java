package com.ums.schedule.domain.request.exception;

import com.ums.schedule.common.code.api.SendRequestErrorCode;
import com.ums.schedule.common.exception.PolicyViolationException;

public class SendRequestDomainException extends PolicyViolationException {
    protected SendRequestDomainException(SendRequestErrorCode errorCode) {
        super(errorCode);
    }

    public static SendRequestDomainException of(SendRequestErrorCode errorCode) {
        return new SendRequestDomainException(errorCode);
    }
}
