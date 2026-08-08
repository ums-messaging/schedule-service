package com.ums.schedule.fixture.email;

import com.ums.schedule.application.ums.email.generator.model.RenderedTemplateContent;

import java.util.UUID;

public class RenderedTemplateContentBuilder {
    private String fileKey;
    private String template;
    private String attachmentName;
    private String downloadName;

    public static RenderedTemplateContentBuilder builder() {
        return new RenderedTemplateContentBuilder();
    }

    private RenderedTemplateContentBuilder() {
        this.fileKey = "%s.html".formatted(UUID.randomUUID().toString());
        this.template = "my template";
        this.attachmentName = "청구서.html";
        this.downloadName = "나의 청구서.html";
    }

    public RenderedTemplateContentBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public RenderedTemplateContentBuilder template(String template) {
        this.template = template;
        return this;
    }

    public RenderedTemplateContentBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public RenderedTemplateContentBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public RenderedTemplateContent build() {
        return new RenderedTemplateContent(
                fileKey,
                template,
                attachmentName,
                downloadName
        );
    }
}
