package com.ums.schedule.domain.exception.validation;

public class InvalidNumberFormatException extends InvalidValueException {
    protected InvalidNumberFormatException(String message) {
        super(message);
    }

    public static InvalidNumberFormatException ofCycleValue() {
        return new InvalidNumberFormatException("주기 값은 숫자 형태여야 합니다.");
    }
}
