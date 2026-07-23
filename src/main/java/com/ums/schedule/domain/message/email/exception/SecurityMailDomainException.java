package com.ums.schedule.domain.message.email.exception;

import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.email.security.PasswordType;
import com.ums.schedule.common.code.email.security.SecurityMailCode;
import com.ums.schedule.common.exception.PolicyViolationException;

public class SecurityMailDomainException extends PolicyViolationException {
    protected SecurityMailDomainException(EmailMessageErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static SecurityMailDomainException of(PasswordType passwordType) {
        return new SecurityMailDomainException(
                EmailMessageErrorCode.SECURITY_POLICY_REQUIRED, passwordType.description());
    }

    public static SecurityMailDomainException of(SecurityMailCode securityMail) {
        return new SecurityMailDomainException(
                EmailMessageErrorCode.SECURITY_POLICY_REQUIRED, securityMail.code());
    }
}
