package com.ums.schedule.common.exception.validate;

import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;

public class InvalidDateFormatException extends ValidateException {
    protected InvalidDateFormatException(String field) {
        super("[%s] 유효하지 않은 날짜 형식입니다.".formatted(field));
    }

    public static InvalidDateFormatException of(String field) {
        return new InvalidDateFormatException(field);
    }

}
