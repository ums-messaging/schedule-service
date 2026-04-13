package com.ums.schedule.domain.request.exception.status;

public abstract class SendRequestStatusException extends SendStatusException {
    private SendRequestStatusException(String message) {
        super(message);
    }

    public static SendRequestStatusException of(String toStatus) {
        return new SendRequestStatusException(String.format("REQUEST -> %s 상태로 변경 불가능합니다.", toStatus));
    }
}
