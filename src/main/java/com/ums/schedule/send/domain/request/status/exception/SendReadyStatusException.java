package com.ums.schedule.send.domain.request.status.exception;

public class SendReadyStatusException extends SendStatusException {
    private SendReadyStatusException(String message) {
        super(message);
    }

    public static SendReadyStatusException of(String toStatus) {
        return new SendReadyStatusException(String.format("Ready -> %s 상태로 변경 불가능합니다.", toStatus));
    }

    public static SendReadyStatusException ofNotReady() {
        throw new SendReadyStatusException("메시지 혹은 대상자 처리가 진행 중 입니다.");
    }
}
