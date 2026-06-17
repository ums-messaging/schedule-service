package com.ums.schedule.domain.sendrequest.message.email.exception;

public class EmailMessageFileKeyMissingException extends EmailMessageException {
    protected EmailMessageFileKeyMissingException(String content) {
        super("%s file key is empty.");
    }

    public static EmailMessageFileKeyMissingException headerOf() {
        return new EmailMessageFileKeyMissingException("header");
    }

    public static EmailMessageFileKeyMissingException bodyOf() {
        return new EmailMessageFileKeyMissingException("body");
    }

    public static EmailMessageFileKeyMissingException footerOf() {
        return new EmailMessageFileKeyMissingException("footer");
    }
}
