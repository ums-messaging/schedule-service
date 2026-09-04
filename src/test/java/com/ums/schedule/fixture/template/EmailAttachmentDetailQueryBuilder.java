package com.ums.schedule.fixture.template;

import com.ums.schedule.application.ums.email.template.query.model.EmailAttachmentDetailQuery;
import com.ums.schedule.common.code.email.AttachmentType;

public class EmailAttachmentDetailQueryBuilder {
    private AttachmentType fileType;
    private String fileKey;
    private String attachmentName;
    private String downloadName;

    public static EmailAttachmentDetailQueryBuilder builder() {
        return new EmailAttachmentDetailQueryBuilder();
    }

    private EmailAttachmentDetailQueryBuilder() {
        this.fileType = AttachmentType.DIRECT;
        this.fileKey = "jang314.pdf";
        this.attachmentName = "${target_name}.pdf";
        this.downloadName = "20260707.pdf";
    }

    public EmailAttachmentDetailQueryBuilder type(AttachmentType type) {
        this.fileType = type;
        return this;
    }

    public EmailAttachmentDetailQueryBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }


    public EmailAttachmentDetailQueryBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public EmailAttachmentDetailQueryBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public EmailAttachmentDetailQuery build() {
        return new EmailAttachmentDetailQuery(
                fileType,
                fileKey,
                attachmentName,
                downloadName
        );
    }
}
