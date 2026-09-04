package com.ums.schedule.common.code.email.security;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum SecurityMailCode implements EnumMapper {
    ENCRYPTION_TYPE(EncryptionTypeEnum.class),
    PASSWORD_HASH(PasswordHashEnum.class),
    PERMISSION_MASK(PermissionMaskEnum.class);

    Class<? extends EnumMapperType> code;

    SecurityMailCode(Class<? extends EnumMapperType> code) {
        this.code = code;
    }

    @Override
    public String key() {
        return this.name();
    }

    @Override
    public Class<? extends EnumMapperType> code() {
        return code;
    }
}
