package com.ums.schedule.fixture.template;

import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;

public class EmailTemplateContentResultBuilder {
    private String format;
    private String section;
    private String contentType;
    private String attachmentName;
    private String downloadName;
    private String baseDir;
    private String fileKeyTemplate;
    private String fileKey;
    private Long fileSize;

    public static EmailTemplateContentResultBuilder builder() {
        return new EmailTemplateContentResultBuilder();
    }

    private EmailTemplateContentResultBuilder() {
        this.attachmentName = "test.pdf";
        this.downloadName = "test.pdf";
        this.fileSize = 100L;
    }

    public EmailTemplateContentResultBuilder header() {
        this.format = "FILE";
        this.section = "header";
        this.fileKey = "header.html";
        return this;
    }
    public EmailTemplateContentResultBuilder body() {
        this.format = "FILE";
        this.section = "body";
        this.fileKey = "body.html";
        return this;
    }

    public EmailTemplateContentResultBuilder attachment() {
        this.format = "FILE";
        this.section = "attachment";
        this.fileKey = "attachment.pdf";
        return this;
    }
    public EmailTemplateContentResultBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public EmailTemplateContentResultBuilder fileKeyTemplate(String fileKeyTemplate) {
        this.fileKeyTemplate = fileKeyTemplate;
        return this;
    }

    public EmailTemplateContentResultBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public EmailTemplateContentResultBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }
    public EmailTemplateContentResultBuilder cover() {
        this.format = "FILE";
        this.section = "cover";
        this.fileKey = "cover.html";
        return this;
    }
    public EmailTemplateContentResultBuilder footer() {
        this.format = "FILE";
        this.section = "footer";
        this.fileKey = "footer.html";
        return this;
    }

    public EmailTemplateContentResult build() {
        return new EmailTemplateContentResult(
                this.format,
                this.section,
                this.contentType,
                this.attachmentName,
                this.downloadName,
                this.fileKeyTemplate,
                this.fileKey,
                this.fileSize
        );
    }
}
