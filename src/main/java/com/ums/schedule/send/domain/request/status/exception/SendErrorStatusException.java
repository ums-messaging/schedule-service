package com.ums.schedule.send.domain.request.status.exception;

public class SendErrorStatusException extends SendStatusException {
    private SendErrorStatusException(String message) {
        super(message);
    }

    public static SendErrorStatusException of(String toStatus) {
        return new SendErrorStatusException(String.format("Fail -> %s 상태로 변경 불가능합니다.", toStatus));
    }
}
