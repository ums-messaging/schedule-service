package com.ums.schedule.fixture.email.attachment;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.common.code.email.AttachmentType;

public class AttachmentContextBuilder {
    private AttachmentType type;
    private String key;
    private String attachmentName;
    private String downloadName;
    private Long fileSize;

    public static AttachmentContextBuilder builder() {
        return new AttachmentContextBuilder();
    }

    private AttachmentContextBuilder() {
        this.type = AttachmentType.DIRECT;
        this.key = "attachment.html";
        this.attachmentName = "첨부파일명.html";
        this.downloadName = "다운로드명.html";
    }

    public AttachmentContextBuilder type(AttachmentType type) {
        this.type = type;
        return this;
    }

    public AttachmentContextBuilder key(String key) {
        this.key = key;
        return this;
    }

    public AttachmentContextBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public AttachmentContextBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public AttachmentContextBuilder fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public AttachmentContext build() {
        return new AttachmentContext(
                type,
                key,
                attachmentName,
                downloadName,
                fileSize
        );
    }
}
