package com.ums.schedule.fixture.email;

import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplateContent;
import freemarker.template.Template;

public class EmailTemplateContentBuilder {
    private Template template;
    private String fileKey;
    private String fileKeyTemplate;
    private String attachmentName;
    private String downloadName;

    public static EmailTemplateContentBuilder builder() {
        return new EmailTemplateContentBuilder();
    }

    private EmailTemplateContentBuilder() {
    }

    public EmailTemplateContentBuilder template(Template template) {
        this.template = template;
        return this;
    }

    public EmailTemplateContentBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public EmailTemplateContentBuilder fileKeyTemplate(String fileKeyTemplate) {
        this.fileKeyTemplate = fileKeyTemplate;
        return this;
    }

    public EmailTemplateContentBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public EmailTemplateContentBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public EmailTemplateContent build() {
        return new EmailTemplateContent(
                template,
                fileKey,
                fileKeyTemplate,
                attachmentName,
                downloadName
        );
    }
}
