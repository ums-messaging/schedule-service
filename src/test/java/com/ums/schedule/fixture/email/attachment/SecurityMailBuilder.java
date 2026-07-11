package com.ums.schedule.fixture.email.attachment;

import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.code.EncryptionTypeEnum;
import com.ums.schedule.domain.message.email.code.PasswordHashEnum;
import com.ums.schedule.domain.message.email.code.PermissionMaskEnum;

public class SecurityMailBuilder {
    private EnumMapperValue encryptionType;
    private EnumMapperValue passwordHash;
    private EnumMapperValue permissionMask;
    private String passwordPolicy;
    private String passwordFormat;

    public static SecurityMailBuilder builder() {
        return new SecurityMailBuilder();
    }

    private SecurityMailBuilder() {
        this.encryptionType = EnumMapperValue.fromEnumMapperType(EncryptionTypeEnum.ASE256);
        this.passwordHash = EnumMapperValue.fromEnumMapperType(PasswordHashEnum.SHA256);
        this.permissionMask = EnumMapperValue.fromEnumMapperType(PermissionMaskEnum.NONE);
        this.passwordPolicy = "birthday";
    }

    public SecurityMailBuilder encryptionType(EnumMapperValue encryptionType) {
        this.encryptionType = encryptionType;
        return this;
    }

    public SecurityMailBuilder passwordHash(EnumMapperValue passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }


    public SecurityMailBuilder passwordFormat(String passwordFormat) {
        this.passwordFormat = passwordFormat;
        return this;
    }

    public SecurityMailBuilder permissionMask(EnumMapperValue permissionMask) {
        this.permissionMask = permissionMask;
        return this;
    }

    public SecurityMailBuilder passwordPolicy(String passwordPolicy) {
        this.passwordPolicy = passwordPolicy;
        return this;
    }

    public SecurityMail build() {
        return new SecurityMail(
                encryptionType,
                passwordHash,
                permissionMask,
                passwordPolicy,
                passwordFormat
        );
    }
}
