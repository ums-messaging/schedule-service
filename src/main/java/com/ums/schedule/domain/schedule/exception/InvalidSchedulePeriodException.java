package com.ums.schedule.domain.schedule.exception;

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
}
