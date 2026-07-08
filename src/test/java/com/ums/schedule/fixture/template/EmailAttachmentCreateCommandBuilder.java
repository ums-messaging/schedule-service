package com.ums.schedule.fixture.template;

import com.ums.schedule.application.template.email.query.model.EmailAttachmentDetailQuery;

public class EmailAttachmentCreateCommandBuilder {
    private String fileKey;
    private String fileKeyTemplate;
    private String attachmentName;
    private String downloadName;

    public static EmailAttachmentCreateCommandBuilder builder() {
        return new EmailAttachmentCreateCommandBuilder();
    }

    private EmailAttachmentCreateCommandBuilder() {
        this.fileKey = "jang314.pdf";
        this.fileKeyTemplate = "${target_id}.pdf";
        this.attachmentName = "${target_name}.pdf";
        this.downloadName = "20260707.pdf";
    }

    public EmailAttachmentCreateCommandBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public EmailAttachmentCreateCommandBuilder fileKeyTemplate(String fileKeyTemplate) {
        this.fileKeyTemplate = fileKeyTemplate;
        return this;
    }

    public EmailAttachmentCreateCommandBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public EmailAttachmentCreateCommandBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public EmailAttachmentDetailQuery build() {
        return new EmailAttachmentDetailQuery(
                fileKey,
                fileKeyTemplate,
                attachmentName,
                downloadName
        );
    }
}
