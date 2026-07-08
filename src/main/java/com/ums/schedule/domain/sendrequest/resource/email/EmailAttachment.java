package com.ums.schedule.domain.sendrequest.resource.email;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.exception.validation.InvalidFileExtensionException;
import com.ums.schedule.common.exception.validation.InvalidFilenameValueException;
import com.ums.schedule.common.util.ValidationUtils;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessage;
import com.ums.schedule.domain.sendrequest.resource.email.policy.AttachmentPolicy;
import com.ums.schedule.domain.sendrequest.resource.email.code.AttachmentEnumMapper;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.application.message.email.model.EmailAttachmentCreateCommand;
import com.ums.schedule.domain.sendrequest.resource.email.policy.SecurityPolicy;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class EmailAttachment {
    @Id
    @Tsid
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "convert_type", nullable = false)
    private ConvertTypeEnum convertType;

    @Embedded
    private SecurityPolicy securityPolicy;

    @Embedded
    private AttachmentPolicy attachmentPolicy;

    private String fileKeyTemplate;
    private String fileKey;
    private Long fileSize;

    @JoinColumn(name = "message_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EmailSendMessage sendMessage;

    public static EmailAttachment of(EmailSendMessage sendMessage, EmailAttachmentCreateCommand command) {
        EmailAttachment message = new EmailAttachment();
        message.assignSendMessage(sendMessage);
        message.createSecurityPolicyAndResolveConvertType(command);
        message.createAttachmentPolicy(command.attachmentName(), command.downloadName());
        message.generateFileMetadata(command.fileKey(), command.fileKeyTemplate(), command.fileSize());
        return message;
    }

    private void assignSendMessage(EmailSendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    private void generateFileMetadata(String fileKey, String fileKeyTemplate, Long fileSize) {
        if(this.convertType != ConvertTypeEnum.NONE) {
            assignFileMetadata(fileKey, fileKeyTemplate, fileSize);
            return;
        }
        assignFileKeyOrFileKeyTemplate(fileKey, fileKeyTemplate, fileSize);
    }

    private void assignFileMetadata(String fileKey, String fileKeyTemplate, Long fileSize) {
        assignFileKeyAndSize(fileKey, fileSize);
        validateAndAssignFileKeyTemplate(fileKeyTemplate);
    }

    private void validateAndAssignFileKeyTemplate(String fileKeyTemplate) {
        assignFileKeyTemplate(fileKeyTemplate);

        validateFileExtension(fileKeyTemplate);
    }

    private void validateFileExtension(String fileKeyTemplate) {
        String extension = extractFileExtension(fileKeyTemplate);
        if(!this.convertType.code().toLowerCase().equals(extension)) {
            throw InvalidFileExtensionException.of(this.convertType.code().toLowerCase());
        }
    }

    private String extractFileExtension(String fileKeyTemplate) {
        String[] extractFileStrs = fileKeyTemplate.split("\\.");
        try {
            return extractFileStrs[1];
        } catch (IndexOutOfBoundsException e) {
            throw InvalidFilenameValueException.of();
        }
    }

    private void assignFileKeyTemplate(String fileKeyTemplate) {
        ValidationUtils.isEmpty("file_key_template", fileKeyTemplate);
        this.fileKeyTemplate = fileKeyTemplate;
    }

    private void assignFileKeyOrFileKeyTemplate(String fileKey, String fileKeyTemplate, Long fileSize) {
        if(!StringUtils.hasText(fileKey)) {
            assignFileKeyTemplate(fileKeyTemplate);
            return;
        }
        assignFileKeyAndSize(fileKey, fileSize);
    }

    private void assignFileKeyAndSize(String fileKey, Long fileSize) {
        assignFileKey(fileKey);
        assignFileSize(fileSize);
    }

    private void assignFileSize(Long fileSize) {
        ValidationUtils.isEmpty("file_size", fileSize);
        this.fileSize = fileSize;
    }

    private void assignFileKey(String fileKey) {
        ValidationUtils.isEmpty("file_key", fileKey);
        this.fileKey = fileKey;
    }


    private void createSecurityPolicyAndResolveConvertType(EmailAttachmentCreateCommand body) {
        this.convertType = resolveConvertType(body.convertType());
        Map<AttachmentEnumMapper, EnumMapperValue> securityMap = body.securityPolicyMap();
        if(hasSecurityPolicy(securityMap)) {
            this.securityPolicy = SecurityPolicy.of(body.passwordFormat(), body.passwordHash(), securityMap);
        }
    }

    private boolean hasSecurityPolicy(Map<AttachmentEnumMapper, EnumMapperValue> securityMap) {
        return !securityMap.isEmpty() && securityMap != null && this.convertType != ConvertTypeEnum.NONE;
    }

    private ConvertTypeEnum resolveConvertType(EnumMapperValue convertType) {
        return ConvertTypeEnum.valueOf(convertType.code());
    }

    private void createAttachmentPolicy(String attachmentName, String downloadName) {
        this.attachmentPolicy = AttachmentPolicy.of(attachmentName, downloadName);
    }


    public boolean hasSecurityPolicy() {
        return this.securityPolicy != null;
    }

    public int getEncryptionLength() {
        return hasSecurityPolicy() ? this.securityPolicy.getEncryptionLength() : 0;
    }

    public boolean getCanModify() {
        return hasSecurityPolicy() ? this.securityPolicy.hasModifyAuth() : false;
    }

    public boolean getCanPrint() {
        return hasSecurityPolicy() ? this.securityPolicy.hasPrintAuth() : false;
    }

    public String resolveSecurityPassword(SendTarget target) {
        return hasSecurityPolicy() ? this.securityPolicy.getTargetPassword(target) : null;
    }

    public String resolveTemplateFileKey(SendTarget target) {
        if(this.convertType != ConvertTypeEnum.NONE) {
            ValidationUtils.isEmpty("file_key_template", fileKeyTemplate);
            return target.parse(this.fileKeyTemplate);
        }
        return StringUtils.hasText(this.fileKeyTemplate) ? target.parse(this.fileKeyTemplate) : null;
    }

    public String resolveAttachmentName(SendTarget target) {
        return target.parse(this.attachmentPolicy.getAttachmentName());
    }

    public String resolveDownloadName(SendTarget target) {
        return target.parse(this.attachmentPolicy.getDownloadName());
    }
}
