package com.ums.schedule.attachment.fixture.builder;

import com.ums.schedule.attachment.code.StorageTypeEnum;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.domain.code.TemplateContentFormatEnum;

import java.util.UUID;

public class EmailContentResponseBuilder {
    private String section;
    private String format = "TEXT";
    private String content;
    private String contentType = "html/text";
    private String attachmentName = "첨부파일 명";
    private String downloadName = "다운로드 명";
    private String storageType = StorageTypeEnum.S3.value();
    private String baseDir = "ums-file-bucket-01";
    private String fileKey = "/template/"+ UUID.randomUUID().toString()+ "/file.html";
    private String fileUrl = "http://";
    private String originalFileName = "original.html";
    private Long fileSize = 1L;

    private EmailContentResponseBuilder() {
    }

    public static EmailContentResponseBuilder builder() {
        return new EmailContentResponseBuilder();
    }
    public EmailContentResponseBuilder section(EmailTemplateSectionEnum section) {
        this.section = section.value();
        return this;
    }

    public EmailContentResponseBuilder format(TemplateContentFormatEnum format) {
        this.format = format.value();
        return this;
    }

    public EmailContentResponseBuilder content(String content) {
        this.content = content;
        return this;
    }

    public EmailContentResponseBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public EmailContentResponseBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }
    public EmailContentResponseBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }
    public EmailContentResponseBuilder storageType(StorageTypeEnum storageType) {
        this.storageType = storageType.value();
        return this;
    }
    public EmailContentResponseBuilder baseDir(String baseDir) {
        this.baseDir = baseDir;
        return this;
    }
    public EmailContentResponseBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }
    public EmailContentResponseBuilder fileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }
    public EmailContentResponseBuilder originalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
        return this;
    }
    public EmailContentResponseBuilder fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }
    public EmailContentResponse build() {
        return new EmailContentResponse(
                this.section,
                this.format,
                this.content,
                this.contentType,
                this.attachmentName,
                this.downloadName,
                this.storageType,
                this.baseDir,
                this.fileKey,
                this.fileUrl,
                this.originalFileName,
                this.fileSize
        );
    }
}
