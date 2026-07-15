package com.ums.schedule.application.exception.email.security;


import com.ums.schedule.application.exception.ApplicationException;
import com.ums.schedule.common.code.email.security.PasswordTypeEnum;
import com.ums.schedule.common.code.email.security.SecurityMailEnumMapper;

public class SecurityMailNotConfiguredException extends ApplicationException {
    protected SecurityMailNotConfiguredException(String code) {
        super("[%s] 설정 값을 가져오는데 실패했습니다.".formatted(code));
    }

    public static SecurityMailNotConfiguredException of(SecurityMailEnumMapper mapper) {
        return new SecurityMailNotConfiguredException(mapper.code().getSimpleName());
    }

    public static SecurityMailNotConfiguredException of(PasswordTypeEnum passwordPolicy) {
        return new SecurityMailNotConfiguredException(passwordPolicy.description());
    }
}
