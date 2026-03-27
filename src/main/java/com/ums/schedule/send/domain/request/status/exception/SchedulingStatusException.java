package com.ums.schedule.send.domain.request.status.exception;

public class SchedulingStatusException extends SendStatusException {
    private SchedulingStatusException(String message) {
        super(message);
    }

    public static SchedulingStatusException of(String toStatus) {
        return new SchedulingStatusException(String.format("Scheduling -> %s 상태로 변경 불가능합니다.", toStatus));
    }
}
