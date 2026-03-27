package com.ums.schedule.send.domain.request.status.exception;

public class SendCompletedStatusException extends SendStatusException {
    private SendCompletedStatusException(String message) {
        super(message);
    }

    public static SendCompletedStatusException of(String toStatus) {
        return new SendCompletedStatusException(String.format("Success -> %s 상태로 변경 불가능합니다.", toStatus));
    }
}
