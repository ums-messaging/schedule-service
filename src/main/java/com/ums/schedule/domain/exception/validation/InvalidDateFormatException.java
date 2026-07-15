package com.ums.schedule.domain.exception.validation;

public class InvalidDateFormatException extends InvalidValueException {
    protected InvalidDateFormatException(String message) {
        super(message);
    }

    public static InvalidDateFormatException ofReservationDate() {
        return new InvalidDateFormatException("올바른 형식의 예약 시간이 아닙니다.");
    }
}
