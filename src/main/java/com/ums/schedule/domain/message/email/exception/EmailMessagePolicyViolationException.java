package com.ums.schedule.domain.message.email.exception;

import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.common.exception.PolicyViolationException;

public class EmailMessagePolicyViolationException extends PolicyViolationException {
    protected EmailMessagePolicyViolationException(EmailMessageErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static EmailMessagePolicyViolationException of(EmailMessageErrorCode errorCode, Object... args) {
        return new EmailMessagePolicyViolationException(errorCode, args);
    }

    public static EmailMessagePolicyViolationException of(EmailMessageSection section) {
        return new EmailMessagePolicyViolationException(EmailMessageErrorCode.NOT_FOUND_FILE_KEY, section.description());
    }

    public static EmailMessagePolicyViolationException of(String fileKey, EmailMessageSection section) {
        return new EmailMessagePolicyViolationException(EmailMessageErrorCode.NOT_FOUND_CONTENT, fileKey, section.description());
    }
}
