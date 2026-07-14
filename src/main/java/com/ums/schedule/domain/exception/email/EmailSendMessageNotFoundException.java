package com.ums.schedule.domain.exception.email;

public class EmailSendMessageNotFoundException extends EmailMessageException {

    protected EmailSendMessageNotFoundException(String message) {
        super(message);
    }

    public static EmailSendMessageNotFoundException of() {
        return new EmailSendMessageNotFoundException("이메일 메시지 정보가 존재하지 않습니다.");
    }
}
