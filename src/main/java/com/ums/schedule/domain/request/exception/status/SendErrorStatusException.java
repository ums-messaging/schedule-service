package com.ums.schedule.domain.request.exception.status;

public class SendErrorStatusException extends SendStatusException {
    private SendErrorStatusException(String message) {
        super(message);
    }

    public static SendErrorStatusException of(String toStatus) {
        return new SendErrorStatusException(String.format("Fail -> %s 상태로 변경 불가능합니다.", toStatus));
    }
}
