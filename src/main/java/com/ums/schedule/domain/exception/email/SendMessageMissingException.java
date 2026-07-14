package com.ums.schedule.domain.exception.email;

public class SendMessageMissingException extends MessagePolicyViolationException {
    protected SendMessageMissingException(String field) {
        super("%s를 조회하는데 실패했습니다.");
    }

    public static SendMessageMissingException of(String field) {
        return new SendMessageMissingException(field);
    }
}
