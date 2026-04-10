package com.ums.schedule.send.application.assembler;

import com.ums.schedule.attachment.application.command.SecurityPolicyCommand;
import com.ums.schedule.attachment.code.AttachmentEnumMapper;
import com.ums.schedule.attachment.code.EncryptionTypeEnum;
import com.ums.schedule.attachment.code.PasswordHashEnum;
import com.ums.schedule.attachment.code.PermissionMaskEnum;
import com.ums.schedule.attachment.domain.AttachmentPolicy;
import com.ums.schedule.attachment.domain.SecurityPolicy;
import com.ums.schedule.common.code.EnumMapper;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperType;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

import static com.ums.schedule.attachment.code.AttachmentEnumMapper.*;
import static com.ums.schedule.attachment.code.AttachmentEnumMapper.PERMISSION_MASK;
import static com.ums.schedule.template.domain.code.ConvertTypeEnum.NONE;

@Component
@RequiredArgsConstructor
public class EmailBodyAssembler {
    private final EnumMapperFactory factory;

    public EmailBody createEmailBody(EmailMessageCommand command) {
        EnumMapperValue convertType = resolveConvertTypeEnum(command);
        if(convertType.code().equals(NONE.code())) {
            return null;
        }
        SecurityPolicy securityPolicy = getSecurityPolicy(command.securityPolicy());
        return EmailBody.of(convertType, securityPolicy);
    }

    private SecurityPolicy getSecurityPolicy(SecurityPolicyCommand command) {
        return SecurityPolicy.of(command.passwordPolicy(), command.passwordHash(), toEnumMapperValue(command));
    }

    private EnumMapperValue resolveConvertTypeEnum(EmailMessageCommand command) {
        return Optional.ofNullable(command.convertType())
                .map(convertType -> factory.findEnumMapperValue(CONVERT_TYPE, convertType))
                .filter(code -> !code.code().equals(NONE.code()))
                .orElse(
                        Optional.ofNullable(command.securityPolicy())
                                .map(cmd -> EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.HTML))
                                .orElse(EnumMapperValue.fromEnumMapperType(NONE))
                );
    }

    private Map<AttachmentEnumMapper, EnumMapperValue> toEnumMapperValue(SecurityPolicyCommand secuCmd) {
        return Map.of(
                ENCRYPTION_TYPE, resolveEnumMapperValue(ENCRYPTION_TYPE, secuCmd.encryptionType(), EncryptionTypeEnum.ASE256),
                PASSWORD_HASH, resolveEnumMapperValue(PASSWORD_HASH, secuCmd.passwordHash(), PasswordHashEnum.SHA256),
                PERMISSION_MASK, resolveEnumMapperValue(PERMISSION_MASK, secuCmd.permissionMask(), PermissionMaskEnum.NONE)
        );
    }

    private EnumMapperValue resolveEnumMapperValue(EnumMapper key, String code, EnumMapperType defaultValue) {
        return Optional.ofNullable(code)
                .filter(cd -> StringUtils.hasText(cd))
                .map(cd -> factory.findEnumMapperValue(key, cd))
                .orElse(EnumMapperValue.fromEnumMapperType(defaultValue));
    }
}
