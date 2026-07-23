package com.ums.schedule.application.ums.email.security.model;

import com.ums.schedule.adapter.api.request.email.request.EmailSecurityPolicyRequest;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.security.PasswordType;
import com.ums.schedule.common.code.email.security.SecurityMailCode;

import java.util.Map;

public record SecurityMailCommand(
        Map<SecurityMailCode, String> policyMap,
        Map<PasswordType, String> passwordTypeMap
) {
    public static SecurityMailCommand of(EmailSecurityPolicyRequest request) {
        Map<SecurityMailCode, String> securityPolicyMap = request.toMap();
        Map<PasswordType, String> passwordTypeMap = request.toPasswordTypeMap();

        return new SecurityMailCommand(
                securityPolicyMap,
                passwordTypeMap
        );
    }

    public SecurityMail toSecurityMail(Map<SecurityMailCode, EnumMapperValue> policyMap, String passwordPolicy) {
        return SecurityMail.of(policyMap, passwordTypeMap, passwordPolicy);
    }
}
