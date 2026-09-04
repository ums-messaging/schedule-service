package com.ums.schedule.application.ums.email.security;

import com.ums.schedule.common.code.email.security.PermissionMaskEnum;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.security.PasswordType;
import com.ums.schedule.common.code.email.security.SecurityMailCode;
import com.ums.schedule.domain.message.email.security.SecurityMailPolicy;

import java.util.Map;
import java.util.Optional;

public record SecurityMail(
        EnumMapperValue encryptionType,
        EnumMapperValue passwordHash,
        EnumMapperValue permissionMask,
        String passwordPolicy,
        String passwordFormat
) {

    public static SecurityMail of(Map<SecurityMailCode, EnumMapperValue> toMap,
                                  Map<PasswordType, String> passwordType,
                                  String defaultPasswordPolicy) {
        return new SecurityMail(
                toMap.get(SecurityMailCode.ENCRYPTION_TYPE),
                toMap.get(SecurityMailCode.PASSWORD_HASH),
                toMap.get(SecurityMailCode.PERMISSION_MASK),
                defaultPasswordPolicy,
                Optional.ofNullable(passwordType)
                        .map(type -> type.get(PasswordType.PASSWORD_FORMAT))
                        .orElse(null)
        );
    }

    public static SecurityMail fromEntity(SecurityMailPolicy policy) {
        return new SecurityMail(
                EnumMapperValue.fromEnumMapperType(policy.getEncryptionType()),
                EnumMapperValue.fromEnumMapperType(policy.getPasswordHash()),
                EnumMapperValue.fromEnumMapperType(policy.getPermissionMask()),
                policy.getPasswordPolicy(),
                policy.getPasswordFormat()
        );
    }
}
