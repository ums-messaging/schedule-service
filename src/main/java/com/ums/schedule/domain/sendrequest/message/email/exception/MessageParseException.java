package com.ums.schedule.domain.sendrequest.message.email.exception;

public class MessageParseException extends MessagePolicyViolationException {

    protected MessageParseException(String message) {
        super(message);
    }

    public static MessageParseException of(String messageId, String targetId) {
        return new MessageParseException("[%s][%s] 메시지 치환 중 오류가 발생했습니다.".formatted(messageId, targetId));
    }
}
