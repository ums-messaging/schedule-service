package com.ums.schedule.schedule.domain.exception;

public class InvalidNumberFormatException extends ScheduleDomainException {
    protected InvalidNumberFormatException(String message) {
        super(message);
    }

    public static InvalidNumberFormatException ofCycleValue() {
        return new InvalidNumberFormatException("주기 값은 숫자 형태여야 합니다.");
    }
}
