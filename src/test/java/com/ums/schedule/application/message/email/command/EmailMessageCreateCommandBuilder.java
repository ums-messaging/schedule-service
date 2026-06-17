package com.ums.schedule.application.message.email.command;

import com.ums.schedule.application.sendrequest.message.email.command.EmailAttachmentCreateCommand;
import com.ums.schedule.application.sendrequest.message.email.command.SecurityPolicyCreateCommand;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.message.email.code.*;
import com.ums.schedule.domain.sendrequest.resource.email.code.*;

import java.util.*;
import java.util.stream.Collectors;

public class EmailMessageCreateCommandBuilder {
    private EnumMapperValue convertType;
    private String attachmentName;
    private String downloadName;
    private String fileKey;
    private String fileKeyTemplate;
    private Long fileSize;
    private Map<AttachmentEnumMapper, EnumMapperValue> securityPolicyMap;
    private String passwordFormat;
    private String passwordHash;

    public EmailMessageCreateCommandBuilder() {
        this.convertType = EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE);
        this.attachmentName = "${targetId}";
        this.downloadName = "${targetName}";
        this.fileKey = "1234567.html";
        this.fileKeyTemplate = "${customer_id}.pdf";
        this.fileSize = 10L;
        this.passwordFormat = null;
        this.securityPolicyMap = Collections.emptyMap();
    }


    public static EmailMessageCreateCommandBuilder builder() {
        return new EmailMessageCreateCommandBuilder();
    }

    public EmailMessageCreateCommandBuilder convertType(ConvertTypeEnum convertType) {
        this.convertType = EnumMapperValue.fromEnumMapperType(convertType);
        return this;
    }

    public EmailMessageCreateCommandBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public EmailMessageCreateCommandBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public EmailMessageCreateCommandBuilder securityPolicyList(EncryptionTypeEnum encryptionType, PasswordHashEnum passwordHash, PermissionMaskEnum permissionMask) {
        this.securityPolicyMap = List.of(
                SecurityPolicyCreateCommand.of(AttachmentEnumMapper.ENCRYPTION_TYPE, EnumMapperValue.fromEnumMapperType(encryptionType)),
                SecurityPolicyCreateCommand.of(AttachmentEnumMapper.PASSWORD_HASH, EnumMapperValue.fromEnumMapperType(passwordHash)),
                SecurityPolicyCreateCommand.of(AttachmentEnumMapper.PERMISSION_MASK, EnumMapperValue.fromEnumMapperType(permissionMask))
        ).stream()
                .filter(command -> command.codeValue() != null)
                .collect(Collectors.toMap(SecurityPolicyCreateCommand::codeKey, command -> command.codeValue()));
        return this;
    }

    public EmailMessageCreateCommandBuilder fileKeyTemplate(String fileKeyTemplate) {
        this.fileKeyTemplate = fileKeyTemplate;
        return this;
    }

    public EmailMessageCreateCommandBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public EmailMessageCreateCommandBuilder fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public EmailMessageCreateCommandBuilder passwordFormat(String passwordFormat) {
        this.passwordFormat = passwordFormat;
        return this;
    }

    public EmailAttachmentCreateCommand build() {
        return new EmailAttachmentCreateCommand(
                convertType,
                attachmentName,
                downloadName,
                fileKeyTemplate,
                fileKey,
                fileSize,
                securityPolicyMap,
                passwordFormat,
                passwordHash
        );
    }
}
