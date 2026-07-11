package com.ums.schedule.domain.sendrequest.resource.email.policy;

import com.ums.schedule.domain.sendrequest.resource.email.code.*;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityPolicy {
    @Enumerated(EnumType.STRING)
    private EncryptionTypeEnum encryptionType;
    @Enumerated(EnumType.STRING)
    private PasswordHashEnum passwordHash;

    @Enumerated(EnumType.STRING)
    private PermissionMaskEnum permissionMask;

    private String passwordPolicy;
    private String passwordFormat;

    public static SecurityPolicy of(String passwordPolicy, String passwordFormat,
                                        Map<SecurityMailEnumMapper, EnumMapperValue> enumMapperMap) {
        SecurityPolicy securityPolicy = new SecurityPolicy();
        securityPolicy.resolveSecurityPolicyEnum(enumMapperMap);
        securityPolicy.definePasswordPolicy(passwordPolicy, passwordFormat);
        return securityPolicy;
    }

    private void definePasswordPolicy(String passwordPolicy, String passwordFormat) {
        this.passwordPolicy = passwordPolicy;
        this.passwordFormat = passwordFormat;
    }

    private void resolveSecurityPolicyEnum(Map<SecurityMailEnumMapper, EnumMapperValue> enumMapperMap) {
        resolveEncryptionType(enumMapperMap.get(SecurityMailEnumMapper.ENCRYPTION_TYPE));
        resolvePasswordHash(enumMapperMap.get(SecurityMailEnumMapper.PASSWORD_HASH));
        resolvePermissionMask(enumMapperMap.get(SecurityMailEnumMapper.PERMISSION_MASK));
    }

    private void resolvePermissionMask(EnumMapperValue permissionMask) {
        this.permissionMask = Optional.ofNullable(permissionMask)
                .map(v -> PermissionMaskEnum.valueOf(v.code()))
                .orElse(PermissionMaskEnum.NONE);
    }

    private void resolvePasswordHash(EnumMapperValue passwordHash) {
        this.passwordHash = Optional.ofNullable(passwordHash)
                .map(v -> PasswordHashEnum.valueOf(v.code()))
                .orElse(PasswordHashEnum.SHA256);
    }

    private void resolveEncryptionType(EnumMapperValue encryptionType) {
        this.encryptionType = Optional.ofNullable(encryptionType)
                .map(v -> EncryptionTypeEnum.valueOf(v.code()))
                .orElse(EncryptionTypeEnum.ASE256);
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
