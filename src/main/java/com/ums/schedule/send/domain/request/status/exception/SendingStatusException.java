package com.ums.schedule.send.domain.request.status.exception;

public class SendingStatusException extends SendStatusException {
    private SendingStatusException(String message) {
        super(message);
    }

    public static SendingStatusException of(String toStatus) {
        return new SendingStatusException(String.format("Sending -> %s 상태로 변경 불가능합니다.", toStatus));
    }
}
