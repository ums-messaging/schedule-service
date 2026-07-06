package com.ums.schedule.application.exception;

public class EmailMessageNotFoundException extends NotFoundException {
    protected EmailMessageNotFoundException(String message) {
        super(message);
    }

    public static EmailMessageNotFoundException of(String messageId) {
        return new EmailMessageNotFoundException("[%s] message가 존재하지 않습니다.".formatted(messageId));
    }
}
