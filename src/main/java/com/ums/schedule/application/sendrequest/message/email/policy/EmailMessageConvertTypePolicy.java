package com.ums.schedule.application.sendrequest.message.email.policy;

import com.ums.schedule.application.sendrequest.message.email.command.EmailAttachmentCreateCommand;
import com.ums.schedule.application.sendrequest.message.email.command.EmailConvertPolicyCommand;
import com.ums.schedule.application.sendrequest.message.email.command.SecurityPolicyCommand;
import com.ums.schedule.application.sendrequest.message.email.result.EmailMessagePolicyResult;
import com.ums.schedule.application.template.email.query.model.EmailTemplateContentResult;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.util.FileUtil;
import com.ums.schedule.domain.sendrequest.resource.email.code.AttachmentEnumMapper;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
import com.ums.schedule.domain.sendrequest.template.code.TemplateEnumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EmailMessageConvertTypePolicy {
    private final EnumMapperFactory mapperFactory;
    private final String uploadPrefix;

    public EmailMessagePolicyResult generateConvertPolicy(EmailConvertPolicyCommand command) {
        SecurityPolicyCommand securityPolicyCommand = toSecurityPolicyCommand(command);
        EnumMapperValue convertType = resolveConvertType(command.convertType(), securityPolicyCommand);
        List<EmailAttachmentCreateCommand> attachmentList;
        Map<EmailTemplateSectionEnum, String> templateMap;
        EmailAttachmentCreateCommand fromBody = null;
        EmailTemplateContentResult body;

        if (ConvertTypeEnum.valueOf(convertType.code()) == ConvertTypeEnum.NONE) {
            body = command.body();
        } else {
            body = command.cover();
            fromBody = EmailAttachmentCreateCommand.bodyOf(convertType, command.body(), generateFileKeyTemplate(convertType), securityPolicyCommand);
        }

        templateMap = toTemplateKeyMap(command.header(), body, command.footer());
        attachmentList = createAttachmentList(fromBody, command.attachmentList());

        return EmailMessagePolicyResult.of(convertType, templateMap, attachmentList);
    }

    private String generateFileKeyTemplate(EnumMapperValue convertType) {
        String extension = convertType.code().toLowerCase();
        return "%s.%s".formatted(FileUtil.generateFilePaths("${targetId}"), extension);
    }

    private List<EmailAttachmentCreateCommand> createAttachmentList(EmailAttachmentCreateCommand fromBody, List<EmailTemplateContentResult> attachments) {
        return Stream.concat(
                Stream.of(fromBody)
                        .filter(Objects::nonNull),
                attachments.stream()
                        .map(EmailAttachmentCreateCommand::of)
        ).toList();
    }

    private SecurityPolicyCommand toSecurityPolicyCommand(EmailConvertPolicyCommand command) {
        Map<AttachmentEnumMapper, EnumMapperValue> securityEnumMap = new EnumMap<>(AttachmentEnumMapper.class);
        putSecurityEnumMap(securityEnumMap, AttachmentEnumMapper.ENCRYPTION_TYPE, command.encryptionType());
        putSecurityEnumMap(securityEnumMap, AttachmentEnumMapper.PASSWORD_HASH, command.passwordHash());
        putSecurityEnumMap(securityEnumMap, AttachmentEnumMapper.PERMISSION_MASK, command.permissionMask());
        return command.toSecurityPolicyCommand(securityEnumMap);
    }

    private void putSecurityEnumMap(Map<AttachmentEnumMapper, EnumMapperValue> securityEnumMap, AttachmentEnumMapper enumKey, String code) {
        Optional.ofNullable(code)
                .map(v -> mapperFactory.findEnumMapperValue(enumKey, code))
                .ifPresent(v -> securityEnumMap.put(enumKey, v));
    }

    private Map<EmailTemplateSectionEnum, String> toTemplateKeyMap(EmailTemplateContentResult header, EmailTemplateContentResult body, EmailTemplateContentResult footer) {
        return Map.of(
                EmailTemplateSectionEnum.HEADER, getFileKey(header),
                EmailTemplateSectionEnum.BODY, getFileKey(body),
                EmailTemplateSectionEnum.FOOTER, getFileKey(footer)
        ).entrySet()
                .stream()
                .filter(entry -> StringUtils.hasText(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String getFileKey(EmailTemplateContentResult content) {
        return Optional.ofNullable(content).map(EmailTemplateContentResult::fileKey).orElse("");
    }

    private EnumMapperValue resolveConvertType(String convertType, SecurityPolicyCommand securityPolicy) {
        return Optional.ofNullable(convertType)
                .map(type -> mapperFactory.findEnumMapperValue(TemplateEnumMapper.CONVERT_TYPE, type))
                .orElse(
                        Optional.ofNullable(securityPolicy)
                                .map(policy -> EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.HTML))
                                .orElse(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE))
                );
    }
}
