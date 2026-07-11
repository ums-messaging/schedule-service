package com.ums.schedule.adapter.api.request.email;

import com.ums.schedule.application.ums.email.security.model.SecurityMailCommand;
import com.ums.schedule.domain.sendrequest.resource.email.code.PasswordTypeEnum;
import com.ums.schedule.domain.sendrequest.resource.email.code.SecurityMailEnumMapper;
import com.ums.schedule.domain.sendrequest.resource.email.policy.SecurityPolicy;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public record EmailSecurityPolicyRequest(
        String encryptionType,
        String passwordPolicy,
        String passwordHash,
        String passwordFormat,
        String permissionMask
) {
    public static EmailSecurityPolicyRequest of(SecurityPolicy securityPolicy) {
        return new EmailSecurityPolicyRequest(
                securityPolicy.getEncryptionType().value(),
                securityPolicy.getPasswordPolicy(),
                securityPolicy.getPasswordHash().value(),
                securityPolicy.getPasswordFormat(),
                securityPolicy.getPermissionMask().value()
        );
    }

    public SecurityMailCommand toCommand() {
        return SecurityMailCommand.of(this);
    }

    public Map<SecurityMailEnumMapper, String> toMap() {
        Map<SecurityMailEnumMapper, String> enumMap = new EnumMap<>(SecurityMailEnumMapper.class);
        putSecurityPolicyMap(enumMap, SecurityMailEnumMapper.ENCRYPTION_TYPE, encryptionType);
        putSecurityPolicyMap(enumMap, SecurityMailEnumMapper.PASSWORD_HASH, passwordHash);
        putSecurityPolicyMap(enumMap, SecurityMailEnumMapper.PERMISSION_MASK, permissionMask);

        return enumMap;
    }

    private void putSecurityPolicyMap(Map<SecurityMailEnumMapper, String> enumMap, SecurityMailEnumMapper mapperKey, String commandValue) {
        if(StringUtils.hasText(commandValue)) {
            enumMap.put(mapperKey, commandValue);
        }
    }

    public Map<PasswordTypeEnum, String> toPasswordTypeMap() {
        Map<PasswordTypeEnum, String> passwordTypeMap = new EnumMap<>(PasswordTypeEnum.class);
        putPasswordTypeMap(passwordTypeMap, PasswordTypeEnum.PASSWORD_POLICY, passwordPolicy());
        putPasswordTypeMap(passwordTypeMap, PasswordTypeEnum.PASSWORD_FORMAT, passwordFormat());
        return Optional.ofNullable(passwordTypeMap)
                .orElseGet(Collections::emptyMap);
    }

    private void putPasswordTypeMap(Map<PasswordTypeEnum, String> map, PasswordTypeEnum type, String commandValue) {
        if(StringUtils.hasText(commandValue)) {
            map.put(type, commandValue);
        }
    }
}
