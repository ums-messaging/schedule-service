package com.ums.schedule.fixture.email.security;

import com.ums.schedule.application.ums.email.security.model.SecurityMailCommand;
import com.ums.schedule.common.code.email.security.PasswordType;
import com.ums.schedule.common.code.email.security.SecurityMailCode;

import java.util.EnumMap;
import java.util.Map;

public class SecurityMailCommandBuilder {
    private Map<SecurityMailCode, String> policyMap;
    private Map<PasswordType, String> passwordTypeMap;

    public static SecurityMailCommandBuilder builder() {
        return new SecurityMailCommandBuilder();
    }

    private SecurityMailCommandBuilder() {
        this.policyMap = new EnumMap<>(SecurityMailCode.class);
        this.passwordTypeMap = new EnumMap<>(PasswordType.class);
    }

    public SecurityMailCommandBuilder policyMap(Map<SecurityMailCode, String> policyMap) {
        this.policyMap = policyMap;
        return this;
    }


    public SecurityMailCommandBuilder passwordTypeMap(Map<PasswordType, String> passwordTypeMap) {
        this.passwordTypeMap = passwordTypeMap;
        return this;
    }

    public Map<SecurityMailCode, String> getPolicyMap() {
        return policyMap;
    }

    public Map<PasswordType, String> getPasswordTypeMap() {
        return passwordTypeMap;
    }

    public SecurityMailCommand build() {
        return new SecurityMailCommand(
           policyMap, passwordTypeMap
        );
    }

}
