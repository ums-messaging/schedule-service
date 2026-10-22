package com.ums.schedule.email.application.message.config;

import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.security.PasswordHashEnum;
import com.ums.schedule.common.code.email.security.PermissionMaskEnum;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

public record SecurityMailConfig(
   EncryptionTypeEnum encryptionType,
   PasswordHashEnum passwordHash,
   PermissionMaskEnum permissionMask
) {
    public static SecurityMailConfig of(EnumMapperValue encryptionType,
                                        EnumMapperValue passwordHash,
                                        EnumMapperValue permissionMask
                                        ) {
        return new SecurityMailConfig(
                EncryptionTypeEnum.valueOf(encryptionType.code()),
                PasswordHashEnum.valueOf(passwordHash.code()),
                PermissionMaskEnum.valueOf(permissionMask.code())
        );
    }
}
