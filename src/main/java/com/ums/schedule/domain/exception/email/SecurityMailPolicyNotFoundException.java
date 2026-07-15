package com.ums.schedule.domain.exception.email;

import com.ums.schedule.common.code.email.security.PasswordTypeEnum;
import com.ums.schedule.common.code.email.security.SecurityMailEnumMapper;

public class SecurityMailPolicyNotFoundException extends SecurityMailViolationPolicyException {
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
