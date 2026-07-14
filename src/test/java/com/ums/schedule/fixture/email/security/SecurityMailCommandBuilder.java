package com.ums.schedule.fixture.email.security;

import com.ums.schedule.application.ums.email.security.model.SecurityMailCommand;
import com.ums.schedule.common.code.email.PasswordTypeEnum;
import com.ums.schedule.common.code.email.SecurityMailEnumMapper;

import java.util.EnumMap;
import java.util.Map;

public class SecurityMailCommandBuilder {
    private Map<SecurityMailEnumMapper, String> policyMap;
    private Map<PasswordTypeEnum, String> passwordTypeMap;

    public static SecurityMailCommandBuilder builder() {
        return new SecurityMailCommandBuilder();
    }

    private SecurityMailCommandBuilder() {
        this.policyMap = new EnumMap<>(SecurityMailEnumMapper.class);
        this.passwordTypeMap = new EnumMap<>(PasswordTypeEnum.class);
    }

    public SecurityMailCommandBuilder policyMap(Map<SecurityMailEnumMapper, String> policyMap) {
        this.policyMap = policyMap;
        return this;
    }


    public SecurityMailCommandBuilder passwordTypeMap(Map<PasswordTypeEnum, String> passwordTypeMap) {
        this.passwordTypeMap = passwordTypeMap;
        return this;
    }

    public Map<SecurityMailEnumMapper, String> getPolicyMap() {
        return policyMap;
    }

    public Map<PasswordTypeEnum, String> getPasswordTypeMap() {
        return passwordTypeMap;
    }

    public SecurityMailCommand build() {
        return new SecurityMailCommand(
           policyMap, passwordTypeMap
        );
    }

}
