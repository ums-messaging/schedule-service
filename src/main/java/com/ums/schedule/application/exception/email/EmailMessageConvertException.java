package com.ums.schedule.application.exception.email;

import com.ums.schedule.application.exception.ApplicationException;

public class EmailMessageConvertException extends ApplicationException {
    protected EmailMessageConvertException(String message) {
        super(message);
    }

    protected EmailMessageConvertException(String message, Throwable e) {
        super(message, e);
    }

    protected EmailMessageConvertException(Throwable e) {
        super("메시지 변환 중 오류가 발생했습니다.", e);
    }

    public static EmailMessageConvertException of(Throwable e) {
        return new EmailMessageConvertException(e);
    }
}
