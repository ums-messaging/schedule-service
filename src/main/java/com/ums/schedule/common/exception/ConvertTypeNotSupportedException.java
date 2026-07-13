package com.ums.schedule.common.exception;

import com.ums.schedule.application.exception.ApplicationException;

public class ConvertTypeNotSupportedException extends ApplicationException {
    protected ConvertTypeNotSupportedException(String message) {
        super(message);
    }

    public static ConvertTypeNotSupportedException of() {
        return new ConvertTypeNotSupportedException("지원하지 않는 변환 타입입니다.");
    }
}
