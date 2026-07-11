package com.ums.schedule.application.ums.email.security;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.code.PasswordTypeEnum;
import com.ums.schedule.domain.sendrequest.resource.email.code.SecurityMailEnumMapper;

import java.util.Map;
import java.util.Optional;

public record SecurityMail(
        EnumMapperValue encryptionType,
        EnumMapperValue passwordHash,
        EnumMapperValue permissionMask,
        String passwordPolicy,
        String passwordFormat
) {

    public static SecurityMail of(Map<SecurityMailEnumMapper, EnumMapperValue> toMap,
                                  Map<PasswordTypeEnum, String> passwordType,
                                  String defaultPasswordPolicy) {
        return new SecurityMail(
                toMap.get(SecurityMailEnumMapper.ENCRYPTION_TYPE),
                toMap.get(SecurityMailEnumMapper.PASSWORD_HASH),
                toMap.get(SecurityMailEnumMapper.PERMISSION_MASK),
                defaultPasswordPolicy,
                Optional.ofNullable(passwordType)
                        .map(type -> type.get(PasswordTypeEnum.PASSWORD_FORMAT))
                        .orElse(null)
        );
    }
}
