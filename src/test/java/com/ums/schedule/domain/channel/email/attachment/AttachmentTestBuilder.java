package com.ums.schedule.domain.channel.email.attachment;

import com.ums.schedule.domain.target.SendTarget;

public class AttachmentTestBuilder {
    private AttachmentPolicy attachmentPolicy = AttachmentPolicyTestBuilder.builder().build();
    private FileMetaData fileMetaData = FileMetaDataTestBuilder.builder().build();
    private SendTarget target;

    public static AttachmentTestBuilder builder() {
        return new AttachmentTestBuilder();
    }

    public AttachmentTestBuilder attachmentPolicy(AttachmentPolicy attachmentPolicy) {
        this.attachmentPolicy = attachmentPolicy;
        return this;
    }

    public AttachmentTestBuilder fileMetaData(FileMetaData fileMetaData) {
        this.fileMetaData = fileMetaData;
        return this;
    }

    public AttachmentTestBuilder sendTarget(SendTarget target) {
        this.target = target;
        return this;
    }

    public Attachment build() {
        return new Attachment(null, attachmentPolicy, fileMetaData, target);
    }
}
