package com.ums.schedule.application.ums.email.security;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.security.PasswordType;
import com.ums.schedule.common.code.email.security.SecurityMailCode;

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

}
