package com.ums.schedule.domain.sendrequest.resource.email.code;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum SecurityMailEnumMapper implements EnumMapper {
    ENCRYPTION_TYPE(EncryptionTypeEnum.class),
    PASSWORD_HASH(PasswordHashEnum.class),
    PERMISSION_MASK(PermissionMaskEnum.class);

    Class<? extends EnumMapperType> code;

    SecurityMailEnumMapper(Class<? extends EnumMapperType> code) {
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
