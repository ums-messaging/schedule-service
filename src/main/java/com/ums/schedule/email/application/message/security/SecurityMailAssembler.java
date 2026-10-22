package com.ums.schedule.email.application.message.security;

import com.ums.schedule.common.code.email.security.*;
import com.ums.schedule.email.adaptor.in.config.MessageConfigProvider;
import com.ums.schedule.email.application.message.config.SecurityMailConfig;
import com.ums.schedule.email.application.message.security.model.SecurityMailCommand;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.email.domain.message.security.SecurityMail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SecurityMailAssembler {
    private final EnumMapperFactory mapperFactory;
    private final MessageConfigProvider provider;

    public SecurityMail assemble(SecurityMailCommand command) {
        SecurityMailConfig config = provider.loadSecurityMail();
        Map<SecurityMailCode, String> policyMap = command.policyMap();

        EncryptionTypeEnum encryptionType = findEncryptionType(config, policyMap.get(SecurityMailCode.ENCRYPTION_TYPE));
        PasswordHashEnum passwordHash = findPasswordHash(config, policyMap.get(SecurityMailCode.PASSWORD_HASH));
        PermissionMaskEnum permissionMask = findPermissionMask(config, policyMap.get(SecurityMailCode.PERMISSION_MASK));

        return SecurityMail.of(encryptionType, passwordHash, permissionMask, PasswordType.PASSWORD_POLICY);
    }

    private EncryptionTypeEnum findEncryptionType(SecurityMailConfig config, String codeValue) {
        if(StringUtils.hasText(codeValue)) {
            EnumMapperValue enumValue = mapperFactory.findEnumMapperValue(SecurityMailCode.ENCRYPTION_TYPE, codeValue);
            return EncryptionTypeEnum.valueOf(enumValue.code());
        }
        return config.encryptionType();
    }
    public PasswordHashEnum findPasswordHash(SecurityMailConfig config, String codeValue) {
        if(StringUtils.hasText(codeValue)) {
            EnumMapperValue enumValue = mapperFactory.findEnumMapperValue(SecurityMailCode.PASSWORD_HASH, codeValue);
            return PasswordHashEnum.valueOf(enumValue.code());
        }
        return config.passwordHash();
    }
    public PermissionMaskEnum findPermissionMask(SecurityMailConfig config, String codeValue) {
        if(StringUtils.hasText(codeValue)) {
            EnumMapperValue enumValue = mapperFactory.findEnumMapperValue(SecurityMailCode.PERMISSION_MASK, codeValue);
            return PermissionMaskEnum.valueOf(enumValue.code());
        }
        return config.permissionMask();
    }


}