package com.ums.schedule.email.adaptor.out.persistence.jpa.entity;

import com.ums.schedule.email.domain.attachment.EmailAttachment;
import com.ums.schedule.email.domain.common.FileMetadata;
import com.ums.schedule.email.domain.common.MultiPartFile;
import com.ums.schedule.orchestrator.domain.event.converter.UuidBinaryConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(
        name = "email_attachment",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="uq_message_file_key",
                        columnNames = {"message_id", "file_key"}
                ),
        }
)
public class EmailAttachmentEntity extends BaseEntity {
    @Id
    @UuidGenerator
    @GeneratedValue
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @Column(name = "type", nullable = false)
    private String type;
    private String contentType;
    private String fileKeyTemplate;
    private String attachmentName;
    private String downloadName;
    private String fileKey;
    private Long fileSize;

    @JoinColumn(name = "message_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private EmailSendMessageEntity message;

    public static EmailAttachmentEntity fromDomain(EmailAttachment domain) {
        EmailAttachmentEntity entity = new EmailAttachmentEntity();
        entity.assignId(entity.getId(), domain.getId());
        entity.assignFileMetadata(domain.getMetadata());
        entity.assignMultipart(domain.getMultipart());
        return entity;
    }

    private void assignFileMetadata(FileMetadata metadata) {
        this.contentType = metadata.contentType();
        this.fileKey = metadata.fileKey();
        this.fileSize = metadata.fileSize();
    }
    private void assignMultipart(MultiPartFile multipart) {
        this.fileKeyTemplate = multipart.fileKeyTemplate();
        this.attachmentName = multipart.attachmentName();
        this.downloadName = multipart.downloadName();
    }
    public void assignSendMessage(EmailSendMessageEntity message) {
        this.message = message;
    }

    public EmailAttachment toDomain() {
        return EmailAttachment.fromEntity(this);
    }
}

