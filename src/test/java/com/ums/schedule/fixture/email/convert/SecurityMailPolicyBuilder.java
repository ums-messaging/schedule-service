package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.security.PasswordHashEnum;
import com.ums.schedule.common.code.email.security.PermissionMaskEnum;
import com.ums.schedule.domain.message.email.security.SecurityMailPolicy;

public class SecurityMailPolicyBuilder {

    private EncryptionTypeEnum encryptionType;
    private PasswordHashEnum passwordHash;
    private PermissionMaskEnum permissionMask;
    private String passwordPolicy;
    private String passwordFormat;

    public static SecurityMailPolicyBuilder builder() {
        return new SecurityMailPolicyBuilder();
    }

    private SecurityMailPolicyBuilder() {
        this.encryptionType = EncryptionTypeEnum.ASE256;
        this.permissionMask = PermissionMaskEnum.NONE;
        this.passwordHash = PasswordHashEnum.SHA256;
    }

    public SecurityMailPolicy build() {
        return new SecurityMailPolicy(
                this.encryptionType,
                this.passwordHash,
                this.permissionMask,
                this.passwordPolicy,
                this.passwordFormat
        );
    }
}
