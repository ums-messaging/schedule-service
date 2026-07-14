package com.ums.schedule.domain.exception.email;

public class MessageTypeNotFoundException extends MessagePolicyViolationException {

    protected MessageTypeNotFoundException(String message) {
        super(message);
    }

    public static MessageTypeNotFoundException of() {
        return new MessageTypeNotFoundException("메시지 타입이 존재하지 않습니다.");
    }
}
