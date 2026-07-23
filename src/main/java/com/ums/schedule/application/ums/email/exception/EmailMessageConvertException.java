package com.ums.schedule.application.ums.email.exception;

import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.api.ErrorCode;


public class EmailMessageConvertException extends EmailPolicyViolationException {
    protected EmailMessageConvertException(Long id, ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static EmailMessageConvertException of(Long id, ErrorCode errorCode, Object... args) {
        return new EmailMessageConvertException(id, errorCode, args);
    }

    public static EmailMessageConvertException of(Long id, Throwable e) {
        return new EmailMessageConvertException(id, EmailMessageErrorCode.NOT_CONVERT_MESSAGE, e.getMessage());
    }
}
