package com.ums.schedule.application.channel.email.creator;

import com.ums.schedule.code.email.*;
import com.ums.schedule.adapter.api.send.email.EmailSecurityPolicyRequest;
import com.ums.schedule.domain.channel.email.security.SecurityPolicy;
import com.ums.schedule.code.EnumMapper;
import com.ums.schedule.code.EnumMapperFactory;
import com.ums.schedule.code.EnumMapperType;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.adapter.api.send.email.EmailSendCreateRequest;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

import static com.ums.schedule.code.email.ConvertTypeEnum.NONE;

@Component
@RequiredArgsConstructor
public class EmailBodyCreator {
    private final EnumMapperFactory factory;

    public EmailBody createEmailBody(EmailSendCreateRequest command) {
        EnumMapperValue convertType = resolveConvertTypeEnum(command);
        if(convertType.code().equals(NONE.code())) {
            return EmailBody.of();
        }
        SecurityPolicy securityPolicy = getSecurityPolicy(command.securityPolicy());
        return EmailBody.of(convertType, securityPolicy);
    }

    private SecurityPolicy getSecurityPolicy(EmailSecurityPolicyRequest command) {
        return SecurityPolicy.of(command.passwordPolicy(), command.passwordHash(), toEnumMapperValue(command));
    }

    private EnumMapperValue resolveConvertTypeEnum(EmailSendCreateRequest command) {
        return Optional.ofNullable(command.convertType())
                .map(convertType -> factory.findEnumMapperValue(TemplateEnumMapper.CONVERT_TYPE, convertType))
                .filter(code -> !code.code().equals(NONE.code()))
                .orElse(
                        Optional.ofNullable(command.securityPolicy())
                                .map(cmd -> EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.HTML))
                                .orElse(EnumMapperValue.fromEnumMapperType(NONE))
                );
    }

    private Map<AttachmentEnumMapper, EnumMapperValue> toEnumMapperValue(EmailSecurityPolicyRequest secuCmd) {
        return Map.of(
                AttachmentEnumMapper.ENCRYPTION_TYPE, resolveEnumMapperValue(AttachmentEnumMapper.ENCRYPTION_TYPE, secuCmd.encryptionType(), EncryptionTypeEnum.ASE256),
                AttachmentEnumMapper.PASSWORD_HASH, resolveEnumMapperValue(AttachmentEnumMapper.PASSWORD_HASH, secuCmd.passwordHash(), PasswordHashEnum.SHA256),
                AttachmentEnumMapper.PERMISSION_MASK, resolveEnumMapperValue(AttachmentEnumMapper.PERMISSION_MASK, secuCmd.permissionMask(), PermissionMaskEnum.NONE)
        );
    }

    private EnumMapperValue resolveEnumMapperValue(EnumMapper key, String code, EnumMapperType defaultValue) {
        return Optional.ofNullable(code)
                .filter(cd -> StringUtils.hasText(cd))
                .map(cd -> factory.findEnumMapperValue(key, cd))
                .orElse(EnumMapperValue.fromEnumMapperType(defaultValue));
    }
}
