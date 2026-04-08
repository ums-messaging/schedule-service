package com.ums.schedule.attachment.application.resolver;

import com.ums.schedule.attachment.application.command.SecurityPolicyCommand;
import com.ums.schedule.attachment.application.handler.EmailBodyHandler;
import com.ums.schedule.attachment.code.AttachmentEnumMapper;
import com.ums.schedule.attachment.code.EncryptionTypeEnum;
import com.ums.schedule.attachment.code.PasswordHashEnum;
import com.ums.schedule.attachment.code.PermissionMaskEnum;
import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.attachment.infrastructure.AwsS3Repository;
import com.ums.schedule.common.code.EnumMapper;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperType;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

import static com.ums.schedule.attachment.code.AttachmentEnumMapper.*;
import static com.ums.schedule.attachment.code.AttachmentEnumMapper.PERMISSION_MASK;
import static com.ums.schedule.template.domain.code.ConvertTypeEnum.NONE;

@Component
@RequiredArgsConstructor
public class EmailBodyCreator implements AttachmentCreator {
    private final EnumMapperFactory factory;
    private final EmailBodyHandler handler;
    private final Configuration configuration;
    private final AwsS3Repository repository;

    public Attachment createAttachment(EmailMessageCommand command, EmailContentResponse content, SendTargetDto targetDto)  {
        Attachment attachment = Attachment.of(resolveConvertTypeEnum(command));
        try {
            attachment.defineSecurityPolicy(command.securityPolicy(), toEnumMapperValue(command.securityPolicy()));
            attachment.defineAttachmentPolicy(targetDto.parse(content.attachmentName()), targetDto.parse(content.downloadName()));
            attachment.defineFileMetadata(handler.handle(attachment, getTemplate(content.fileKey()), targetDto));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return attachment;
    }

    private Template getTemplate(String fileKey) {
        InputStreamReader reader = repository.getFileContent(fileKey);
        Template template = null;
        try {
            template = new Template("BODY", reader, configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return template;
    }

    public boolean supports(EnumMapperValue section, EmailMessageCommand command) {
        return section.code().equals(EmailTemplateSectionEnum.BODY.code()) &&
                resolveConvertTypeEnum(command) != EnumMapperValue.fromEnumMapperType(NONE);
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
