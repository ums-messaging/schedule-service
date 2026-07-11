package com.ums.schedule.domain.message.exception;

import com.ums.schedule.domain.message.email.code.PasswordTypeEnum;
import com.ums.schedule.domain.message.email.code.SecurityMailEnumMapper;

public class SecurityMailPolicyNotFoundException extends SecurityMailViolationPolicyException{
    protected SecurityMailPolicyNotFoundException(String policyName) {
        super("[%s] 존재하지 않는 정책입니다.".formatted(policyName));
    }

    public static SecurityMailPolicyNotFoundException of(SecurityMailEnumMapper enumMapper) {
        return new SecurityMailPolicyNotFoundException(enumMapper.key());
    }

    public static SecurityMailPolicyNotFoundException of(PasswordTypeEnum enumMapper) {
        return new SecurityMailPolicyNotFoundException(enumMapper.code());
    }
}
