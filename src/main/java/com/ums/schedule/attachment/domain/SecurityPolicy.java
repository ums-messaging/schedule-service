package com.ums.schedule.attachment.domain;

import com.ums.schedule.attachment.code.AttachmentEnumMapper;
import com.ums.schedule.attachment.code.EncryptionTypeEnum;
import com.ums.schedule.attachment.code.PasswordHashEnum;
import com.ums.schedule.attachment.code.PermissionMaskEnum;
import com.ums.schedule.attachment.exception.PasswordPolicyRequiredException;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.code.EncodingTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Map;

import static com.ums.schedule.attachment.code.AttachmentEnumMapper.ENCRYPTION_TYPE;
import static com.ums.schedule.attachment.code.AttachmentEnumMapper.PASSWORD_HASH;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityPolicy {
    private EncryptionTypeEnum encryptionType;
    private PasswordHashEnum passwordHash;
    private PermissionMaskEnum permissionMask;

    private String passwordPolicy;
    private String passwordFormat;

    public static SecurityPolicy of(String passwordPolicy, String passwordFormat,
                                        Map<AttachmentEnumMapper, EnumMapperValue> enumMapperMap) {
        SecurityPolicy securityPolicy = new SecurityPolicy();
        securityPolicy.resolveSecurityPolicyEnum(enumMapperMap);
        securityPolicy.definePasswordPolicy(passwordPolicy, passwordFormat);
        securityPolicy.validate();
        return securityPolicy;
    }

    private void definePasswordPolicy(String passwordPolicy, String passwordFormat) {
        this.passwordPolicy = passwordPolicy;
        this.passwordFormat = passwordFormat;
    }

    private void resolveSecurityPolicyEnum(Map<AttachmentEnumMapper, EnumMapperValue> enumMapperMap) {
        resolveEncryptionType(enumMapperMap.get(ENCRYPTION_TYPE));
        resolvePasswordHash(enumMapperMap.get(PASSWORD_HASH));
        resolvePermissionMask(enumMapperMap.get(AttachmentEnumMapper.PERMISSION_MASK));

    }

    private void resolvePermissionMask(EnumMapperValue permissionMask) {
        this.permissionMask = PermissionMaskEnum.valueOf(permissionMask.code());
    }

    private void resolvePasswordHash(EnumMapperValue passwordHash) {
        this.passwordHash = PasswordHashEnum.valueOf(passwordHash.code());
    }

    private void resolveEncryptionType(EnumMapperValue encryptionType) {
        this.encryptionType = EncryptionTypeEnum.valueOf(encryptionType.code());
    }

    protected void validate() {
        if(!StringUtils.hasText(passwordPolicy)) {
            throw PasswordPolicyRequiredException.ofPasswordPolicy();
        }
    }

}
