package com.ums.schedule.application.ums.email.security;

import com.ums.schedule.application.exception.email.security.SecurityMailNotConfiguredException;
import com.ums.schedule.application.ums.email.config.SecurityMailProperties;
import com.ums.schedule.application.ums.email.security.model.SecurityMailCommand;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.PasswordTypeEnum;
import com.ums.schedule.common.code.email.SecurityMailEnumMapper;
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
        Map<SecurityMailEnumMapper, String> policyMap = command.policyMap();
        Map<SecurityMailEnumMapper, EnumMapperValue> determinedPolicyMap = determinePolicyMap(policyMap);
        String passwordPolicy = getPasswordPolicyOrDefaultValue(command.passwordTypeMap());

        return command.toSecurityMail(determinedPolicyMap, passwordPolicy);
    }

    private String getPasswordPolicyOrDefaultValue(Map<PasswordTypeEnum, String> passwordTypeMap) {
        return Optional.ofNullable(passwordTypeMap)
                .map(map -> map.getOrDefault(PasswordTypeEnum.PASSWORD_POLICY, validateAndGetDefaultPasswordValue()))
                .filter(policy -> StringUtils.hasText(policy))
                .orElseGet(() -> validateAndGetDefaultPasswordValue());
    }

    private String validateAndGetDefaultPasswordValue() {
        String defaultValue = properties.defaultPasswordPolicy();
        if(!StringUtils.hasText(defaultValue)) {
            throw SecurityMailNotConfiguredException.of(PasswordTypeEnum.PASSWORD_POLICY);
        }
        return defaultValue;
    }

    private Map<SecurityMailEnumMapper, EnumMapperValue> determinePolicyMap(Map<SecurityMailEnumMapper, String> policyMap) {
        Map<SecurityMailEnumMapper, String> defaultPolicyMap = getDefaultConfigureMap();
        return Arrays.stream(SecurityMailEnumMapper.class.getEnumConstants())
                .collect(Collectors.toMap(
                        k -> k,
                        e -> toMapOrDefaultValueToMap(policyMap, defaultPolicyMap, e)
                        )
                );

    }

    private EnumMapperValue toMapOrDefaultValueToMap(Map<SecurityMailEnumMapper, String> policyMap, Map<SecurityMailEnumMapper, String> defaultPolicyMap, SecurityMailEnumMapper e) {
        return Optional.ofNullable(defaultPolicyMap.get(e))
                .filter(StringUtils::hasText)
                .map(defaultValue ->
                        toPolicyMap(policyMap, e, defaultValue)
                )
                .orElseThrow(() -> SecurityMailNotConfiguredException.of(e));
    }

    private EnumMapperValue toPolicyMap(Map<SecurityMailEnumMapper, String> policyMap, SecurityMailEnumMapper e, String defaultValue) {
        return Optional.ofNullable(policyMap)
                .filter(policy -> StringUtils.hasText(policy.get(e)))
                .map(policy -> policy.getOrDefault(e, defaultValue))
                .map(v -> mapperFactory.findEnumMapperValue(e, v))
                .orElseGet(() -> mapperFactory.findEnumMapperValue(e, defaultValue));
    }

    private Map<SecurityMailEnumMapper, EnumMapperValue> findMapperDefaultValue(Map<SecurityMailEnumMapper, String> defaultPolicyMap) {
        return defaultPolicyMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> mapperFactory.findEnumMapperValue(entry.getKey(), entry.getValue()))
                );
    }

    private Map<SecurityMailEnumMapper, EnumMapperValue> findMapperPolicyValue(Map<SecurityMailEnumMapper, String> policyMap, Map<SecurityMailEnumMapper, String> defaultPolicyMap) {
        return policyMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> mapperFactory.findEnumMapperValue(entry.getKey(), getOrDefault(policyMap, defaultPolicyMap, entry.getKey()))
                ));
    }

    private Map<SecurityMailEnumMapper, String> getDefaultConfigureMap() {
        Map<SecurityMailEnumMapper, String> enumMap = new EnumMap<>(SecurityMailEnumMapper.class);
        putSecurityPolicyMap(enumMap, SecurityMailEnumMapper.ENCRYPTION_TYPE, properties.defaultEncryptType());
        putSecurityPolicyMap(enumMap, SecurityMailEnumMapper.PASSWORD_HASH, properties.defaultPasswordHash());
        putSecurityPolicyMap(enumMap, SecurityMailEnumMapper.PERMISSION_MASK, properties.defaultPermissionMask());
        return enumMap;
    }

    private void putSecurityPolicyMap(Map<SecurityMailEnumMapper, String> enumMap, SecurityMailEnumMapper mapperKey, String defaultValue) {
        if(StringUtils.hasText(defaultValue)) {
            enumMap.put(mapperKey, defaultValue);
            return;
        }
        throw SecurityMailNotConfiguredException.of(mapperKey);
    }

    private String getOrDefault(Map<SecurityMailEnumMapper, String> policyMap, Map<SecurityMailEnumMapper, String> defaultPolicyMap, SecurityMailEnumMapper key) {
        return Optional.ofNullable(policyMap)
                .map(policy -> getConfiguredDefaultValue(policyMap, defaultPolicyMap, key))
                .orElseThrow();
    }

    private String getConfiguredDefaultValue(Map<SecurityMailEnumMapper, String> policyMap, Map<SecurityMailEnumMapper, String> defaultPolicyMap, SecurityMailEnumMapper key) {
        return Optional.ofNullable(defaultPolicyMap)
                .filter(defaultMap -> StringUtils.hasText(defaultMap.get(key)))
                .map(defaultMap -> policyMap.getOrDefault(key, defaultMap.get(key)))
                .orElseThrow();
    }
}
