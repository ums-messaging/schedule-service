package com.ums.schedule.application.ums.email.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.api.SecurityMailErrorCode;
import com.ums.schedule.common.code.email.security.PasswordType;
import com.ums.schedule.common.code.email.security.SecurityMailCode;
import com.ums.schedule.common.exception.NotConfiguredException;

public class SecurityMailNotConfiguredException extends NotConfiguredException {

    private SecurityMailNotConfiguredException(ErrorCode errorCode) {
        super(errorCode);
    }

    private SecurityMailNotConfiguredException(String args) {
        super(args);
    }

    public static SecurityMailNotConfiguredException of(SecurityMailCode code) {
        return new SecurityMailNotConfiguredException(code.code().getSimpleName());
    }

    public static SecurityMailNotConfiguredException of(PasswordType passwordType) {
        return new SecurityMailNotConfiguredException(passwordType.description());
    }

    public static SecurityMailNotConfiguredException of(SecurityMailErrorCode errorCode) {
        return new SecurityMailNotConfiguredException(errorCode);
    }
}
