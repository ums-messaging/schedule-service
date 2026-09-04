package com.ums.schedule.application.ums.email.security;

import com.ums.schedule.application.ums.email.config.SecurityMailProperties;
import com.ums.schedule.application.ums.email.exception.SecurityMailNotConfiguredException;
import com.ums.schedule.application.ums.email.security.model.SecurityMailCommand;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.security.PasswordType;
import com.ums.schedule.common.code.email.security.SecurityMailCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SecurityMailAssembler {
    private final EnumMapperFactory mapperFactory;
    private final SecurityMailProperties properties;

    public SecurityMail assemble(SecurityMailCommand command) {
        Map<SecurityMailCode, String> policyMap = command.policyMap();
        Map<SecurityMailCode, EnumMapperValue> determinedPolicyMap = determinePolicyMap(policyMap);
        String passwordPolicy = getPasswordPolicyOrDefaultValue(command.passwordTypeMap());

        return command.toSecurityMail(determinedPolicyMap, passwordPolicy);
    }

    private String getPasswordPolicyOrDefaultValue(Map<PasswordType, String> passwordTypeMap) {
        return Optional.ofNullable(passwordTypeMap)
                .map(map -> map.getOrDefault(PasswordType.PASSWORD_POLICY, validateAndGetDefaultPasswordValue()))
                .filter(policy -> StringUtils.hasText(policy))
                .orElseGet(() -> validateAndGetDefaultPasswordValue());
    }

    private String validateAndGetDefaultPasswordValue() {
        String defaultValue = properties.getDefaultPasswordPolicy();
        if(!StringUtils.hasText(defaultValue)) {
            throw SecurityMailNotConfiguredException.of(PasswordType.PASSWORD_POLICY);
        }
        return defaultValue;
    }

    private Map<SecurityMailCode, EnumMapperValue> determinePolicyMap(Map<SecurityMailCode, String> policyMap) {
        Map<SecurityMailCode, String> defaultPolicyMap = getDefaultConfigureMap();
        return Arrays.stream(SecurityMailCode.class.getEnumConstants())
                .collect(Collectors.toMap(
                        k -> k,
                        e -> toMapOrDefaultValueToMap(policyMap, defaultPolicyMap, e)
                        )
                );

    }

    private EnumMapperValue toMapOrDefaultValueToMap(Map<SecurityMailCode, String> policyMap, Map<SecurityMailCode, String> defaultPolicyMap, SecurityMailCode code) {
        return Optional.ofNullable(defaultPolicyMap.get(code))
                .filter(StringUtils::hasText)
                .map(defaultValue ->
                        toPolicyMap(policyMap, code, defaultValue)
                )
                .orElseThrow(() -> SecurityMailNotConfiguredException.of(code));
    }

    private EnumMapperValue toPolicyMap(Map<SecurityMailCode, String> policyMap, SecurityMailCode e, String defaultValue) {
        return Optional.ofNullable(policyMap)
                .filter(policy -> StringUtils.hasText(policy.get(e)))
                .map(policy -> policy.getOrDefault(e, defaultValue))
                .map(v -> mapperFactory.findEnumMapperValue(e, v))
                .orElseGet(() -> mapperFactory.findEnumMapperValue(e, defaultValue));
    }

    private Map<SecurityMailCode, String> getDefaultConfigureMap() {
        Map<SecurityMailCode, String> enumMap = new EnumMap<>(SecurityMailCode.class);
        putSecurityPolicyMap(enumMap, SecurityMailCode.ENCRYPTION_TYPE, properties.getDefaultEncryptType());
        putSecurityPolicyMap(enumMap, SecurityMailCode.PASSWORD_HASH, properties.getDefaultPasswordHash());
        putSecurityPolicyMap(enumMap, SecurityMailCode.PERMISSION_MASK, properties.getDefaultPermissionMask());
        return enumMap;
    }

    private void putSecurityPolicyMap(Map<SecurityMailCode, String> enumMap, SecurityMailCode mapperKey, String defaultValue) {
        if(StringUtils.hasText(defaultValue)) {
            enumMap.put(mapperKey, defaultValue);
            return;
        }
        throw SecurityMailNotConfiguredException.of(mapperKey);
    }

    private String getConfiguredDefaultValue(Map<SecurityMailCode, String> policyMap, Map<SecurityMailCode, String> defaultPolicyMap, SecurityMailCode key) {
        return Optional.ofNullable(defaultPolicyMap)
                .filter(defaultMap -> StringUtils.hasText(defaultMap.get(key)))
                .map(defaultMap -> policyMap.getOrDefault(key, defaultMap.get(key)))
                .orElseThrow();
    }
}