package com.ums.schedule.fixture.email.attachment;

import com.ums.schedule.domain.target.message.AttachmentPayload;

public class AttachmentPayloadBuilder {
    private String fileKey;
    private String attachmentName;
    private String downloadName;

    public static AttachmentPayloadBuilder builder() {
        return new AttachmentPayloadBuilder();
    }

    private AttachmentPayloadBuilder() {

    }

    public AttachmentPayloadBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public AttachmentPayloadBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public AttachmentPayloadBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public AttachmentPayload build() {
        return new AttachmentPayload(
                fileKey,
                attachmentName,
                downloadName
        );
    }
}
