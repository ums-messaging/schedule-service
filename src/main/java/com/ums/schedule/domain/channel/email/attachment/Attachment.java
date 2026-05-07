package com.ums.schedule.domain.channel.email.attachment;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.application.channel.email.dto.AttachmentDto;
import com.ums.schedule.domain.UuidBinaryConverter;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.util.UuidUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "email_attachment",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="uq_target_file_key",
                        columnNames = {"target_id", "file_key"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment {
    @Id
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @Embedded
    private AttachmentPolicy attachmentPolicy;

    @Embedded
    private FileMetaData fileMetaData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private SendTarget target;

    public static Attachment of(AttachmentDto dto, SendTarget target) {
        Attachment attachment = new Attachment();
        attachment.applySendTarget(target);
        attachment.defineAttachmentPolicy(dto.getAttachmentPolicy());
        attachment.defineFileMetadata(dto.getFileMetaData());
        return attachment;
    }

    private Attachment defineAttachmentPolicy(AttachmentPolicy policy) {
        String attachmentName = this.target.parse(policy.getAttachmentName());
        String downloadName = this.target.parse(policy.getDownloadName());
        this.attachmentPolicy = AttachmentPolicy.of(attachmentName, downloadName);
        return this;
    }

    public Attachment defineFileMetadata(FileMetaData fileMetaData) {
        this.fileMetaData = fileMetaData;
        return this;
    }

    public Attachment applySendTarget(SendTarget target) {
        this.target = target;
        return this;
    }

    @PrePersist
    public void prePersist() {
        if(this.id == null) {
            this.id = UuidCreator.getTimeOrdered();
        }
    }
}
