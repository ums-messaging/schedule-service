package com.ums.schedule.email.application.message.model;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.security.SecurityMailCode;

public record SecurityPolicyCreateCommand(
        SecurityMailCode codeKey,
        EnumMapperValue codeValue
) {
    public static SecurityPolicyCreateCommand of(SecurityMailCode codeKey, EnumMapperValue codeValue) {
        if(codeValue == null) {
            return new SecurityPolicyCreateCommand(codeKey, null);
        }
        return new SecurityPolicyCreateCommand(codeKey, codeValue);
    }
}
