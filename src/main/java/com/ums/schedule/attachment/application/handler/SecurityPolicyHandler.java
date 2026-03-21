package com.ums.schedule.attachment.application.handler;

import com.ums.schedule.attachment.application.command.SecurityPolicyCommand;
import com.ums.schedule.attachment.code.AttachmentEnumMapper;
import com.ums.schedule.attachment.code.EncryptionTypeEnum;
import com.ums.schedule.attachment.code.PasswordHashEnum;
import com.ums.schedule.attachment.code.PermissionMaskEnum;
import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.common.code.EnumMapper;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperType;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

import static com.ums.schedule.attachment.code.AttachmentEnumMapper.*;
import static com.ums.schedule.template.domain.code.ConvertTypeEnum.NONE;

@Component
@RequiredArgsConstructor
public class SecurityPolicyHandler implements AttachmentHandler {
    private final EnumMapperFactory factory;

    public Attachment handle(EmailMessageCommand command, EmailContentResponse body) {
        EnumMapperValue convertType = resolveConvertTypeEnum(command);
        Attachment attachment = Attachment.of(convertType);
        return Optional.ofNullable(command.securityPolicy())
                .map(cmd -> attachment.defineSecurityPolicy(cmd, toEnumMapperValue(cmd)))
                .orElse(attachment);
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

    private EnumMapperValue resolveConvertTypeEnum(EmailMessageCommand command) {
        return Optional.ofNullable(command.convertType())
                        .filter(code -> !code.toUpperCase().equals("NONE"))
                        .map(code -> factory.findEnumMapperValue(CONVERT_TYPE, command.convertType()))
                        .orElse(
                                Optional.ofNullable(command.securityPolicy())
                                        .map(cmd -> EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.HTML))
                                        .orElse(EnumMapperValue.fromEnumMapperType(NONE))

                        );
    }

}
