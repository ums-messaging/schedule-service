package com.ums.schedule.domain.message.email.attachment;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentCreateCommand;
import com.ums.schedule.common.code.api.AttachmentErrorCode;
import com.ums.schedule.common.util.ValidationUtils;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.domain.message.email.exception.AttachmentPolicyViolationException;
import com.ums.schedule.domain.target.SendTarget;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class EmailAttachment {
    @Id
    @Tsid
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "convert_type", nullable = false)
    private ConvertType convertType;

    @Embedded
    private SecurityMailPolicy securityPolicy;

    private String attachmentName;
    private String downloadName;

    private String fileKeyTemplate;
    private String fileKey;
    private Long fileSize;

    @JoinColumn(name = "message_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EmailSendMessage sendMessage;

    public static EmailAttachment of(AttachmentCreateCommand context) {
        EmailAttachment message = new EmailAttachment();
        message.assignSendMessage(context.sendMessage());
        message.createAttachmentPolicy(context.attachmentName(), context.downloadName());
        message.resolveConvertedAttachment(context);
        return message;
    }

    private void resolveConvertedAttachment(AttachmentCreateCommand context) {
        assignConvertType(context.convertType());
        initializeByConvertType(context);
    }

    private void initializeByConvertType(AttachmentCreateCommand context) {
        ConvertType convertType = context.convertType();
        if(convertType == ConvertType.NONE) {
            initializeAttachment(context);
            return;
        }
        initializeConvertInfo(context);
    }

    private void initializeAttachment(AttachmentCreateCommand context) {
        Map<AttachmentType, String> typeMap = Optional.ofNullable(context.keyMap())
                .filter(map -> !map.isEmpty())
                .orElseThrow(() -> AttachmentPolicyViolationException.of(AttachmentErrorCode.FILE_KEY_MAP_IS_NULL));
        if(typeMap.containsKey(AttachmentType.DIRECT)) {
            initializeMetadata(typeMap.get(AttachmentType.DIRECT), context.fileSize());
            return;
        }
        assignFileKeyTemplate(typeMap.get(AttachmentType.TEMPLATE));
    }

    private void initializeConvertInfo(AttachmentCreateCommand context) {
        Map<AttachmentType, String> typeMap = Optional.ofNullable(context.keyMap())
                .filter(map -> !map.isEmpty())
                .orElseThrow(() -> AttachmentPolicyViolationException.of(AttachmentErrorCode.FILE_KEY_INFO_EMPTY));
        initializeMetadata(typeMap.get(AttachmentType.DIRECT), context.fileSize());
        assignSecurityPolicy(context.securityMail());
        assignFileKeyTemplate(typeMap.get(AttachmentType.TEMPLATE));
        validateFileTemplateExtension(typeMap.get(AttachmentType.DIRECT), AttachmentType.DIRECT, ConvertType.HTML);
        validateFileTemplateExtension(typeMap.get(AttachmentType.TEMPLATE),AttachmentType.TEMPLATE, context.convertType());
    }

    private void initializeMetadata(String fileKey, Long fileSize) {
        assignFileKey(fileKey);
        assignFileSize(fileSize);
    }


    private void assignConvertType(ConvertType convertType) {
        this.convertType = convertType;
    }

    private void assignSecurityPolicy(SecurityMailPolicy securityMail) {
        this.securityPolicy = Optional.ofNullable(securityMail)
                .orElse(null);
    }

    private void assignSendMessage(EmailSendMessage sendMessage) {
        this.sendMessage = Objects.requireNonNull(sendMessage, "email_send_message");
        sendMessage.addAttachments(this);
    }

    private void validateFileTemplateExtension(String fileKey, AttachmentType type, ConvertType convertType) {
        String extension = extractFileExtension(fileKey);
        if(!StringUtils.hasText(extension) || !extension.equals(convertType.description())) {
            throw AttachmentPolicyViolationException.of(type, convertType);
        }
    }

    private String extractFileExtension(String fileKeyTemplate) {
        String[] extractFileStrs = fileKeyTemplate.split("\\.");
        if(extractFileStrs.length < 2) {
            return null;
        }
        return extractFileStrs[1];
    }

    private void assignFileKeyTemplate(String fileKeyTemplate) {
        if(!StringUtils.hasText(fileKeyTemplate)) {
            throw AttachmentPolicyViolationException.of(AttachmentErrorCode.FILE_KEY_TEMPLATE_EMPTY);
        }
        this.fileKeyTemplate = fileKeyTemplate;
    }


    private void assignFileSize(Long fileSize) {
        this.fileSize = Optional.ofNullable(fileSize)
                .orElseThrow(() -> AttachmentPolicyViolationException.of(AttachmentErrorCode.FILE_SIZE_EMPTY));
    }

    private void assignFileKey(String fileKey) {
        if(!StringUtils.hasText(fileKey)) {
            throw AttachmentPolicyViolationException.of(AttachmentErrorCode.FILE_KEY_EMPTY);
        }
        this.fileKey = fileKey;
    }

    private void createAttachmentPolicy(String attachmentName, String downloadName) {
        assignAttachmentName(attachmentName);
        assignDownloadName(downloadName);
    }

    private void assignDownloadName(String downloadName) {
        this.downloadName = Objects.requireNonNull(downloadName, "download_name");
    }

    private void assignAttachmentName(String attachmentName) {
        this.attachmentName = Objects.requireNonNull(attachmentName, "attachment_name");
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
        if(this.convertType != ConvertType.NONE) {
            ValidationUtils.isEmpty("file_key_template", fileKeyTemplate);
            return target.parse(this.fileKeyTemplate);
        }
        return StringUtils.hasText(this.fileKeyTemplate) ? target.parse(this.fileKeyTemplate) : null;
    }

    public String resolveAttachmentName(SendTarget target) {
        return target.parse(this.attachmentName);
    }

    public String resolveDownloadName(SendTarget target) {
        return target.parse(this.downloadName);
    }
}
