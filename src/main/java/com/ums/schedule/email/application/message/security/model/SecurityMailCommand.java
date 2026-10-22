package com.ums.schedule.email.application.message.security.model;

import com.ums.schedule.email.adaptor.in.rest.message.request.SecurityMailRequest;
import com.ums.schedule.common.code.email.security.PasswordType;
import com.ums.schedule.common.code.email.security.SecurityMailCode;

import java.util.Map;

public record SecurityMailCommand(
        Map<SecurityMailCode, String> policyMap,
        Map<PasswordType, String> passwordTypeMap
) {
    public static SecurityMailCommand of(SecurityMailRequest request) {
        Map<SecurityMailCode, String> securityPolicyMap = request.toMap();
        Map<PasswordType, String> passwordTypeMap = request.toPasswordTypeMap();

        return new SecurityMailCommand(
                securityPolicyMap,
                passwordTypeMap
        );
    }

}
