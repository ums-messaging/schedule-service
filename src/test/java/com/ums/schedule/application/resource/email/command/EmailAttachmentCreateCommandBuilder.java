package com.ums.schedule.application.resource.email.command;

import com.ums.schedule.application.message.email.model.AttachmentCreateCommand;
import com.ums.schedule.common.code.email.*;
import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.security.PasswordHashEnum;
import com.ums.schedule.common.code.email.security.PermissionMaskEnum;
import com.ums.schedule.common.code.email.security.SecurityMailCode;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

import java.util.HashMap;
import java.util.Map;

public class EmailAttachmentCreateCommandBuilder {
    private EnumMapperValue convertType;

    private String attachmentName;
    private String downloadName;

    private String fileKeyTemplate;
    private String fileKey;
    private Long fileSize;

    private EncryptionTypeEnum encryptionType;
    private PasswordHashEnum passwordHash;
    private PermissionMaskEnum permissionMask;

    private String passwordPolicy;
    private String passwordFormat;

    public static EmailAttachmentCreateCommandBuilder builder() {
        return new EmailAttachmentCreateCommandBuilder();
    }

    private EmailAttachmentCreateCommandBuilder() {
        this.convertType = EnumMapperValue.fromEnumMapperType(ConvertType.NONE);
        this.attachmentName = "file_attachment_name";
        this.downloadName = "file_download_name";

        this.fileKey = "template.html";
        this.fileKeyTemplate = "${variable}.html";
        this.fileSize = 9999L;
    }

    public EmailAttachmentCreateCommandBuilder convertType(EnumMapperValue convertType) {
        this.convertType = convertType;
        return this;
    }

    public EmailAttachmentCreateCommandBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public EmailAttachmentCreateCommandBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public EmailAttachmentCreateCommandBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public EmailAttachmentCreateCommandBuilder fileKeyTemplate(String fileKeyTemplate) {
        this.fileKeyTemplate = fileKeyTemplate;
        return this;
    }

    public EmailAttachmentCreateCommandBuilder fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public EmailAttachmentCreateCommandBuilder encryptionType(EncryptionTypeEnum encryptionType) {
        this.encryptionType = encryptionType;
        return this;
    }

    public EmailAttachmentCreateCommandBuilder passwordHash(PasswordHashEnum passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public EmailAttachmentCreateCommandBuilder permissionMask(PermissionMaskEnum permissionMask) {
        this.permissionMask = permissionMask;
        return this;
    }

    public EmailAttachmentCreateCommandBuilder passwordFormat(String passwordFormat) {
        this.passwordFormat = passwordFormat;
        return this;
    }

    public AttachmentCreateCommand build() {
        Map<SecurityMailCode, EnumMapperValue> securityMap = new HashMap<>();
        if(encryptionType != null) {
            securityMap.put(SecurityMailCode.ENCRYPTION_TYPE, EnumMapperValue.fromEnumMapperType(encryptionType));
        }
        if(passwordHash != null) {
            securityMap.put(SecurityMailCode.PASSWORD_HASH, EnumMapperValue.fromEnumMapperType(passwordHash));
        }
        if(permissionMask != null) {
            securityMap.put(SecurityMailCode.PERMISSION_MASK, EnumMapperValue.fromEnumMapperType(permissionMask));
        }

        return new AttachmentCreateCommand(
                this.convertType,
                this.attachmentName,
                this.downloadName,
                this.fileKeyTemplate,
                this.fileKey,
                this.fileSize,
                securityMap,
                this.passwordPolicy,
                this.passwordFormat
        );
    }

}
