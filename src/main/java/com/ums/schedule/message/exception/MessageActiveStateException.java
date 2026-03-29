package com.ums.schedule.message.exception;

import com.ums.schedule.message.domain.status.MessageActiveState;

public class MessageActiveStateException extends MessageStateException {

    protected MessageActiveStateException(String message) {
        super(message);
    }

    public static MessageActiveStateException of(String fromStatus) {
        return new MessageActiveStateException(String.format("%s -> ACTIVE 상태로 변경할 수 없습니다.", fromStatus));
    }
}