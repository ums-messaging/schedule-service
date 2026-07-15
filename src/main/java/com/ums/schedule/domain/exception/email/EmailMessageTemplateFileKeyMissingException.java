package com.ums.schedule.domain.exception.email;

import com.ums.schedule.common.code.email.EmailMessageSection;

public class EmailMessageTemplateFileKeyMissingException extends EmailMessagePolicyViolationException {
    protected EmailMessageTemplateFileKeyMissingException(EmailMessageSection section) {
        super("[%s_key] 템플릿 키가 존재하지 않습니다.".formatted(section.code().toLowerCase()));
    }

    public static EmailMessageTemplateFileKeyMissingException of(EmailMessageSection section) {
        return new EmailMessageTemplateFileKeyMissingException(section);
    }
}
