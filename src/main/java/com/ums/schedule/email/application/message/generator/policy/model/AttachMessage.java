package com.ums.schedule.email.application.message.generator.policy.model;

import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.security.PasswordHashEnum;
import com.ums.schedule.common.code.email.security.PasswordType;
import com.ums.schedule.common.code.email.security.PermissionMaskEnum;
import com.ums.schedule.email.domain.attachment.EmailAttachment;
import com.ums.schedule.email.domain.common.FileMetadata;
import com.ums.schedule.email.domain.common.MultiPartFile;
import com.ums.schedule.email.domain.message.ConvertMessage;
import com.ums.schedule.email.domain.message.security.SecurityMail;

public record AttachMessage(
        String id,
        AttachmentType attachmentType,
        ConvertType convertType,
        String contentType,
        String fileKey,
        String fileKeyTemplate,
        String attachmentName,
        String downloadName,
        EncryptionTypeEnum encryptionType,
        PasswordHashEnum passwordHash,
        PasswordType passwordType,
        PermissionMaskEnum permissionMask
) {
    public static AttachMessage of(EmailAttachment attachment) {
        FileMetadata metadata = attachment.getMetadata();
        MultiPartFile multipart = attachment.getMultipart();
        return new AttachMessage(
                attachment.getId(),
                attachment.getType(),
                ConvertType.NONE,
                metadata.contentType(),
                metadata.fileKey(),
                multipart.fileKeyTemplate(),
                multipart.attachmentName(),
                multipart.downloadName(),
                EncryptionTypeEnum.NONE,
                null,
                null,
                null
        );
    }

    public static AttachMessage of( String messageId,
                                    SecurityMail securityMail,
                                    ConvertMessage convertMessage,
                                    FileMetadata metadata) {
        MultiPartFile file = convertMessage.metadata();
        return new AttachMessage(
                messageId,
                AttachmentType.DIRECT,
                convertMessage.convertType(),
                metadata.contentType(),
                metadata.fileKey(),
                file.fileKeyTemplate(),
                file.attachmentName(),
                file.downloadName(),
                securityMail.encryptionType(),
                securityMail.passwdHash(),
                securityMail.passwdType(),
                securityMail.permissionMask()
        );
    }
}
