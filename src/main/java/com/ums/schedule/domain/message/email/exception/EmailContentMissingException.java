package com.ums.schedule.domain.message.email.exception;

import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.common.exception.PolicyViolationException;

public class EmailContentMissingException extends PolicyViolationException {
    private EmailContentMissingException(EmailMessageSection section) {
        super(EmailMessageErrorCode.NOT_FOUND_CONTENT, section.description());
    }
    private EmailContentMissingException(String fileKey, EmailMessageSection section) {
        super(EmailMessageErrorCode.NOT_FOUND_CONTENT, fileKey, section.description());
    }

    public static EmailContentMissingException of(String fileKey, EmailMessageSection section) {
        return new EmailContentMissingException(fileKey, section);
    }

    public static EmailContentMissingException of(EmailMessageSection section) {
        return new EmailContentMissingException(section);
    }
}
