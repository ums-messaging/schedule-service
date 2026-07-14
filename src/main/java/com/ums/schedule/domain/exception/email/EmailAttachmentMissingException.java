package com.ums.schedule.domain.exception.email;

public class EmailAttachmentMissingException extends EmailAttachmentException {
    public EmailAttachmentMissingException(String field) {
        super("[%s] 필수 값입니다. ".formatted(field));
    }

    public static EmailAttachmentMissingException of(String field) {
        return new EmailAttachmentMissingException(field);
    }

}
