package com.ums.schedule.domain.exception;

public class InvalidScheduleStatusException  extends  ScheduleDomainException {
    protected InvalidScheduleStatusException(String message) {
        super(message);
    }


    public static InvalidScheduleStatusException changeToStatus(String from, String to) {
        String message = String.format("%s에서 %s 상태로 변경 불가합니다.", from, to);
        return new InvalidScheduleStatusException(message);
    }
}
