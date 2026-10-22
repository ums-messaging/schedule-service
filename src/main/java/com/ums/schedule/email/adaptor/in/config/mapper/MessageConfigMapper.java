package com.ums.schedule.email.adaptor.in.config.mapper;

import com.ums.schedule.common.code.email.EmailCode;
import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.security.PasswordHashEnum;
import com.ums.schedule.common.code.email.security.PermissionMaskEnum;
import com.ums.schedule.common.code.email.security.SecurityMailCode;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.email.adaptor.in.config.properties.MessageProperties;
import com.ums.schedule.email.application.message.config.SecurityMailConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageConfigMapper {
    private final EnumMapperFactory factory;
    public SecurityMailConfig toSecurityConfig(MessageProperties properties) {
        MessageProperties.SecurityProperties securityProperties = properties.getSecurityProperties();

        EnumMapperValue encryptionType = findSecurityCode(securityProperties, SecurityMailCode.ENCRYPTION_TYPE, EnumMapperValue.fromEnumMapperType(EncryptionTypeEnum.ASE256));
        EnumMapperValue passwordHash = findSecurityCode(securityProperties, SecurityMailCode.PASSWORD_HASH, EnumMapperValue.fromEnumMapperType(PasswordHashEnum.SHA256));
        EnumMapperValue permissionMask = findSecurityCode(securityProperties, SecurityMailCode.PERMISSION_MASK, EnumMapperValue.fromEnumMapperType(PermissionMaskEnum.NONE));

        return SecurityMailConfig.of(
                encryptionType, passwordHash, permissionMask
        );
    }

    private EnumMapperValue findSecurityCode(MessageProperties.SecurityProperties securityProperties, SecurityMailCode permissionMask, EnumMapperValue NONE) {
        return Optional.ofNullable(securityProperties.getEncryptionType())
                .map(v -> factory.findEnumMapperValue(permissionMask, v))
                .orElse(NONE);
    }
}
