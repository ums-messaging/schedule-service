package com.ums.schedule.application.ums.email.template.exception;

import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.email.EmailUploadPrefixType;
import com.ums.schedule.common.exception.NotConfiguredException;

public class EmailTemplateNotConfiguredException extends NotConfiguredException {
    public EmailTemplateNotConfiguredException(EmailMessageErrorCode errorCode) {
        super(errorCode);
    }

    private EmailTemplateNotConfiguredException(EmailUploadPrefixType prefixType) {
        super(prefixType.description());
    }

    public static EmailTemplateNotConfiguredException of(EmailUploadPrefixType prefixType) {
        return new EmailTemplateNotConfiguredException(prefixType);
    }

    public static EmailTemplateNotConfiguredException of(EmailMessageErrorCode errorCode) {
        return new EmailTemplateNotConfiguredException(errorCode);
    }
}
