package com.ums.schedule.application.ums.email.exception;

import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.email.ConvertType;


public class EmailMessageConvertException extends EmailPolicyViolationException {
    private EmailMessageConvertException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static EmailMessageConvertException of(Throwable e) {
        return new EmailMessageConvertException(EmailMessageErrorCode.NOT_CONVERT_MESSAGE, e.getMessage());
    }

    public static EmailMessageConvertException of(EmailMessageErrorCode errorCode, ConvertType convertType, String... args) {
        return new EmailMessageConvertException(errorCode, convertType.code(), args);
    }

    public static EmailMessageConvertException of(EmailMessageErrorCode errorCode, String... args) {
        return new EmailMessageConvertException(errorCode, args);
    }
}
