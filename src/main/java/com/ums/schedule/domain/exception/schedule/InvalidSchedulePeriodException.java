package com.ums.schedule.domain.exception.schedule;

public class InvalidSchedulePeriodException extends ScheduleDomainException {
    private InvalidSchedulePeriodException(String message) {
        super(message);
    }

    public static InvalidSchedulePeriodException startAtBeforeNow() {
        return new InvalidSchedulePeriodException("스케쥴 시작 시각은 오늘 날짜 이후여야 합니다.");
    }

    public static InvalidSchedulePeriodException  endAtAfterStartAt() {
        return new InvalidSchedulePeriodException("스케쥴 종료 시각은 시작 시각보다 1일 이후여야 합니다.");
    }

    public static InvalidSchedulePeriodException outOfSchedulePeriod() {
        return new InvalidSchedulePeriodException("현재 시각이 스케줄 허용 시간 범위에 포함되지 않습니다.");
    }

    public static InvalidSchedulePeriodException requiredSchedulePeriod() {
        return new InvalidSchedulePeriodException("스케쥴 기간은 필수 값 입니다.");
    }

    public static InvalidSchedulePeriodException invalidFormat() {
        return new InvalidSchedulePeriodException("유효한 형식의 날짜 형식이 아닙니다.");
    }

    public static InvalidSchedulePeriodException expired() {
        return new InvalidSchedulePeriodException("종료된 스케쥴 입니다.");
    }
}
