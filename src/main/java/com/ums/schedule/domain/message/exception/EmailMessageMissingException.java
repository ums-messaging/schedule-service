package com.ums.schedule.domain.message.exception;

public class EmailMessageMissingException extends EmailMessageException {
    protected EmailMessageMissingException(String content) {
        super("%s content is empty.".formatted(content));
    }

    public static EmailMessageMissingException headerOf() {
        return new EmailMessageMissingException("header");
    }

    public static EmailMessageMissingException bodyOf() {
        return new EmailMessageMissingException("body");
    }

    public static EmailMessageMissingException footerOf() {
        return new EmailMessageMissingException("footer");
    }
}
