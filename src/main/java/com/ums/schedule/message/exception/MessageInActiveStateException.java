package com.ums.schedule.message.exception;

public class MessageInActiveStateException extends MessageStateException {
    protected MessageInActiveStateException(String message) {
        super(message);
    }

    public static MessageInActiveStateException of(String fromStatus) {
        return new MessageInActiveStateException(String.format("%s -> ACTIVE 상태로 변경할 수 없습니다.", fromStatus));
    }
}
