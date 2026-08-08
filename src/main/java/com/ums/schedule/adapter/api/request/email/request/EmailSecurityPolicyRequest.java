package com.ums.schedule.adapter.api.request.email.request;

import com.ums.schedule.application.ums.email.security.model.SecurityMailCommand;
import com.ums.schedule.common.code.email.security.PasswordType;
import com.ums.schedule.common.code.email.security.SecurityMailCode;
import com.ums.schedule.domain.message.email.security.SecurityMailPolicy;
import jakarta.validation.constraints.NotBlank;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public record EmailSecurityPolicyRequest(
        String encryptionType,
        @NotBlank(message = "EMAIL_SEND_REQUEST:PASSWORD_POLICY_REQUIRED")
        String passwordPolicy,
        String passwordHash,
        String passwordFormat,
        String permissionMask
) {
    public static EmailSecurityPolicyRequest of(SecurityMailPolicy securityPolicy) {
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

    public Map<SecurityMailCode, String> toMap() {
        Map<SecurityMailCode, String> enumMap = new EnumMap<>(SecurityMailCode.class);
        putSecurityPolicyMap(enumMap, SecurityMailCode.ENCRYPTION_TYPE, encryptionType);
        putSecurityPolicyMap(enumMap, SecurityMailCode.PASSWORD_HASH, passwordHash);
        putSecurityPolicyMap(enumMap, SecurityMailCode.PERMISSION_MASK, permissionMask);

        return enumMap;
    }

    private void putSecurityPolicyMap(Map<SecurityMailCode, String> enumMap, SecurityMailCode mapperKey, String commandValue) {
        if(StringUtils.hasText(commandValue)) {
            enumMap.put(mapperKey, commandValue);
        }
    }

    public Map<PasswordType, String> toPasswordTypeMap() {
        Map<PasswordType, String> passwordTypeMap = new EnumMap<>(PasswordType.class);
        putPasswordTypeMap(passwordTypeMap, PasswordType.PASSWORD_POLICY, passwordPolicy());
        putPasswordTypeMap(passwordTypeMap, PasswordType.PASSWORD_FORMAT, passwordFormat());
        return Optional.ofNullable(passwordTypeMap)
                .orElseGet(Collections::emptyMap);
    }

    private void putPasswordTypeMap(Map<PasswordType, String> map, PasswordType type, String commandValue) {
        if(StringUtils.hasText(commandValue)) {
            map.put(type, commandValue);
        }
    }
}
