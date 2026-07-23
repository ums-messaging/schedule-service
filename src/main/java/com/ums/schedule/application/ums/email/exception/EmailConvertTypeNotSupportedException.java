package com.ums.schedule.application.ums.email.exception;

import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailCode;
import com.ums.schedule.common.exception.NotSupportedException;

public class EmailConvertTypeNotSupportedException extends NotSupportedException {
    protected EmailConvertTypeNotSupportedException(String type) {
        super(EmailCode.CONVERT_TYPE, type);
    }

    public static EmailConvertTypeNotSupportedException of(ConvertType convertType) {
        return new EmailConvertTypeNotSupportedException(convertType.code());
    }
    public static EmailConvertTypeNotSupportedException of(String convertType) {
        return new EmailConvertTypeNotSupportedException(convertType);
    }
}
