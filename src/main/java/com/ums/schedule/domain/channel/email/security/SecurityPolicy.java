package com.ums.schedule.domain.channel.email.security;

import com.ums.schedule.code.email.AttachmentEnumMapper;
import com.ums.schedule.code.email.EncryptionTypeEnum;
import com.ums.schedule.code.email.PasswordHashEnum;
import com.ums.schedule.code.email.PermissionMaskEnum;
import com.ums.schedule.domain.channel.email.exception.PasswordPolicyRequiredException;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.domain.target.SendTarget;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Map;

@Getter
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
        resolveEncryptionType(enumMapperMap.get(AttachmentEnumMapper.ENCRYPTION_TYPE));
        resolvePasswordHash(enumMapperMap.get(AttachmentEnumMapper.PASSWORD_HASH));
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

    public String getTargetPassword(SendTarget target) {
        Map<String, Object> dataParam = target.getDataParam();
        String password = String.valueOf(dataParam.get(this.passwordPolicy));

        return password;
    }

    public int getEncryptionLength() {
        return Integer.parseInt(this.encryptionType.description());
    }

    public boolean hasModifyAuth() {
        if(this.permissionMask == PermissionMaskEnum.ALL || this.permissionMask == PermissionMaskEnum.WRITE) {
            return true;
        }
        return false;
    }

    public boolean hasPrintAuth() {
        if(this.permissionMask == PermissionMaskEnum.ALL || this.permissionMask == PermissionMaskEnum.PRINT) {
            return true;
        }
        return false;
    }
}
