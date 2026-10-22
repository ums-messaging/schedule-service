package com.ums.schedule.email.domain.attachment;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailAttachmentEntity;
import com.ums.schedule.email.application.attachment.model.AttachmentCreateCommand;
import com.ums.schedule.email.domain.common.FileMetadata;
import com.ums.schedule.email.domain.common.MultiPartFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailAttachment {
    private String id;
    private AttachmentType type;
    private FileMetadata metadata;
    private MultiPartFile multipart;
    private String messageId;

    public static EmailAttachment of(AttachmentCreateCommand command) {
        EmailAttachment attachment = new EmailAttachment();
        attachment.assignType(command.type());
        if(command.type() == AttachmentType.DIRECT) {
            attachment.assignFileMetadata(command.contentType(), command.fileKey(), command.fileSize());
            attachment.assignAttachmentAndDownloadName(command.attachmentName(), command.downloadName());
        } else {
            attachment.assignFileKeyTemplate(command.fileKey(), command.attachmentName(), command.downloadName());
        }
        return attachment;
    }

    private void assignFileKeyTemplate(String fileKey, String attachmentName, String downloadName) {
        this.multipart = new  MultiPartFile(fileKey, attachmentName, downloadName);
    }

    private void assignAttachmentAndDownloadName(String attachmentName, String downloadName) {
        this.multipart = new MultiPartFile(null, attachmentName, downloadName);
    }


    private void assignType(AttachmentType type) {
        this.type = type;
    }

    private void assignFileMetadata(String contentType, String fileKey, Long fileSize) {
        this.metadata = new FileMetadata(contentType, fileKey, fileSize);
        if(!StringUtils.hasText(metadata.contentType())) {
            // 예외
        }
    }
    private boolean hasTemplateTypeData(AttachmentType type, MultiPartFile multiPart) {
        return type == AttachmentType.TEMPLATE && StringUtils.hasText(multiPart.fileKeyTemplate());
    }

    public static EmailAttachment fromEntity(EmailAttachmentEntity entity) {
        return new EmailAttachment(entity);
    }

    private EmailAttachment(EmailAttachmentEntity entity) {
        this.id = UuidCreator.toString(entity.getId());
        this.type = AttachmentType.valueOf(entity.getType());
        this.metadata = FileMetadata.of(entity.getContentType(), entity.getFileKey(), entity.getFileSize());
        this.multipart = new MultiPartFile(entity.getFileKeyTemplate(), entity.getAttachmentName(), entity.getDownloadName());
        this.messageId = UuidCreator.toString(entity.getMessage().getId());
    }

    public EmailAttachmentEntity toEntity() {
        return EmailAttachmentEntity.fromDomain(this);
    }
}
