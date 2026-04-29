package com.ums.schedule.domain.schedule.exception;

public class ScheduleNameRequiredException extends ScheduleDomainException {

    protected ScheduleNameRequiredException(String message) {
        super(message);
    }

    public static ScheduleNameRequiredException of() {
        return new ScheduleNameRequiredException("스케쥴 명은 필수 값 입니다.");
    }
}
