package com.ums.schedule.application.ums.email.security.model;

import com.ums.schedule.adapter.api.request.email.EmailSecurityPolicyRequest;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.code.PasswordTypeEnum;
import com.ums.schedule.domain.message.email.code.SecurityMailEnumMapper;

import java.util.Map;

public record SecurityMailCommand(
        Map<SecurityMailEnumMapper, String> policyMap,
        Map<PasswordTypeEnum, String> passwordTypeMap
) {
    public static SecurityMailCommand of(EmailSecurityPolicyRequest request) {
        Map<SecurityMailEnumMapper, String> securityPolicyMap = request.toMap();
        Map<PasswordTypeEnum, String> passwordTypeMap = request.toPasswordTypeMap();

        return new SecurityMailCommand(
                securityPolicyMap,
                passwordTypeMap
        );
    }

    public SecurityMail toSecurityMail(Map<SecurityMailEnumMapper, EnumMapperValue> policyMap, String passwordPolicy) {
        return SecurityMail.of(policyMap, passwordTypeMap, passwordPolicy);
    }
}
