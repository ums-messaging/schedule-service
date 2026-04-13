package com.ums.schedule.domain.channel.email.attachment;

import com.ums.schedule.application.channel.email.dto.AttachmentDto;
import com.ums.schedule.domain.target.SendTarget;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Attachment {
    private AttachmentPolicy attachmentPolicy;
    private FileMetaData fileMetaData;
    private SendTarget target;

    public static Attachment of(AttachmentDto dto, SendTarget target) {
        Attachment attachment = new Attachment();
        attachment.applySendTarget(target);
        attachment.defineAttachmentPolicy(dto.getAttachmentPolicy());
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
        target.getAttachment().add(this);
        this.target = target;
        return this;
    }
}
