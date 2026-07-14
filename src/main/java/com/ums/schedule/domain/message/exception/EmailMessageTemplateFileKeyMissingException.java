package com.ums.schedule.domain.message.exception;

import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;

public class EmailMessageTemplateFileKeyMissingException extends EmailMessagePolicyViolationException {
    protected EmailMessageTemplateFileKeyMissingException(EmailTemplateSectionEnum section) {
        super("[%s_key] 템플릿 키가 존재하지 않습니다.".formatted(section.code().toLowerCase()));
    }

    public static EmailMessageTemplateFileKeyMissingException of(EmailTemplateSectionEnum section) {
        return new EmailMessageTemplateFileKeyMissingException(section);
    }
}
