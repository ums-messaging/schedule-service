package com.ums.schedule.domain.message.email.exception;

import com.ums.schedule.common.code.email.EmailRequiredValue;
import com.ums.schedule.common.exception.RequiredValueMissingException;

public class EmailMessageValueMissingException extends RequiredValueMissingException {
    protected EmailMessageValueMissingException(EmailRequiredValue requiredValue) {
        super(requiredValue.code(), requiredValue.description());
    }

    public static EmailMessageValueMissingException of(EmailRequiredValue requiredValue) {
        return new EmailMessageValueMissingException(requiredValue);
    }

}
