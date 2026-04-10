package com.ums.schedule.attachment.domain;

import com.ums.schedule.attachment.application.model.AttachmentDto;
import com.ums.schedule.send.domain.target.EmailSendTarget;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import com.ums.schedule.template.domain.email.EmailAttachment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Attachment {
    private AttachmentPolicy attachmentPolicy;
    private FileMetaData fileMetaData;
    private EmailSendTarget target;

    public static Attachment of(AttachmentDto dto, EmailSendTarget target) {
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

    public Attachment applySendTarget(EmailSendTarget target) {
        target.getAttachment().add(this);
        this.target = target;
        return this;
    }
}
