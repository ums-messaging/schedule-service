package com.ums.schedule.application.message.email.model;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.security.SecurityMailEnumMapper;

public record SecurityPolicyCreateCommand(
        SecurityMailEnumMapper codeKey,
        EnumMapperValue codeValue
) {
    public static SecurityPolicyCreateCommand of(SecurityMailEnumMapper codeKey, EnumMapperValue codeValue) {
        if(codeValue == null) {
            return new SecurityPolicyCreateCommand(codeKey, null);
        }
        return new SecurityPolicyCreateCommand(codeKey, codeValue);
    }
}
