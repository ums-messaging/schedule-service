package com.ums.schedule.domain.message.email;

import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.email.*;
import com.ums.schedule.common.code.email.SecurityMailPolicyNotFoundException;
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
public class SecurityMailPolicy {
    @Enumerated(EnumType.STRING)
    private EncryptionTypeEnum encryptionType;

    @Enumerated(EnumType.STRING)
    private PasswordHashEnum passwordHash;

    @Enumerated(EnumType.STRING)
    private PermissionMaskEnum permissionMask;

    private String passwordPolicy;
    private String passwordFormat;

    public static SecurityMailPolicy of(SecurityMail securityMail) {
        SecurityMailPolicy securityPolicy = new SecurityMailPolicy();
        securityPolicy.resolveEncryptionType(securityMail.encryptionType());
        securityPolicy.resolvePasswordHash(securityMail.passwordHash());
        securityPolicy.resolvePermissionMask(securityMail.permissionMask());
        securityPolicy.resolvePasswordTypePolicy(securityMail.passwordPolicy(), securityMail.passwordFormat());
        return securityPolicy;
    }

    private void resolvePasswordTypePolicy(String passwordPolicy, String passwordFormat) {
        this.passwordFormat = passwordFormat;
        this.passwordPolicy = validateAndGetPasswordPolicy(passwordPolicy);
    }

    private String validateAndGetPasswordPolicy(String passwordPolicy) {
        if (!StringUtils.hasText(passwordPolicy)) {
            throw SecurityMailPolicyNotFoundException.of(PasswordTypeEnum.PASSWORD_POLICY);
        }
        return passwordPolicy;
    }


    private void resolvePermissionMask(EnumMapperValue permissionMask) {
        this.permissionMask = Optional.ofNullable(permissionMask)
                .map(v -> PermissionMaskEnum.valueOf(v.code()))
                .orElseThrow(() -> SecurityMailPolicyNotFoundException.of(SecurityMailEnumMapper.PERMISSION_MASK));
    }

    private void resolvePasswordHash(EnumMapperValue passwordHash) {
        this.passwordHash = Optional.ofNullable(passwordHash)
                .map(v -> PasswordHashEnum.valueOf(v.code()))
                .orElseThrow(() -> SecurityMailPolicyNotFoundException.of(SecurityMailEnumMapper.PASSWORD_HASH));
    }

    private void resolveEncryptionType(EnumMapperValue encryptionType) {
        this.encryptionType = Optional.ofNullable(encryptionType)
                .map(v -> EncryptionTypeEnum.valueOf(v.code()))
                .orElseThrow(() -> SecurityMailPolicyNotFoundException.of(SecurityMailEnumMapper.ENCRYPTION_TYPE));
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
