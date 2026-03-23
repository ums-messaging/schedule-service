package com.ums.schedule.schedule.domain.exception;

public class InvalidCycleValueException extends ScheduleDomainException {
    private InvalidCycleValueException(String message) {
        super(message);
    }

    public static InvalidCycleValueException toMonth() {
        return new InvalidCycleValueException("월 주기 값은 1~12월 단위의 값만 입력 가능합니다.");
    }

    public static InvalidCycleValueException  toMinute() {
        return new InvalidCycleValueException("분 주기 값은 1~59분 단위의 값만 입력 가능합니다.");
    }

    public static InvalidCycleValueException toHour() {
        return new InvalidCycleValueException("시간 단뒤의 주기 값은 1~12 단위의 값만 입력 가능합니다.");
    }

    public static InvalidCycleValueException toDay() {
        return new InvalidCycleValueException("일 단위의 주기 값은 1~31 단위의 값만 입력 가능합니다.");
    }

    public static InvalidCycleValueException toReservationDate() {
        return new InvalidCycleValueException("예약 시간은 현재 시각보다 한 시간 이후어야 합니다.");
    }

    public static InvalidCycleValueException compareToReservationDate() {
        return new InvalidCycleValueException("예약 시간은 스케쥴 기간 안에 포함되어야 합니다.");
    }
}
