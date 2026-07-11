package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.convert.resolver.model.AttachmentResolveCommand;

public class AttachmentResolveCommandBuilder {
    private String fileKey;
    private String fileKeyTemplate;

    public static AttachmentResolveCommandBuilder builder() {
        return new AttachmentResolveCommandBuilder();
    }

    private AttachmentResolveCommandBuilder() {
        this.fileKey = "attachment.html";
        this.fileKeyTemplate = "${template}.html";
    }

    public AttachmentResolveCommandBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public AttachmentResolveCommandBuilder fileKeyTemplate(String fileKeyTemplate) {
        this.fileKeyTemplate = fileKeyTemplate;
        return this;
    }

    public AttachmentResolveCommand build() {
        return new AttachmentResolveCommand(
                fileKey,
                fileKeyTemplate
        );
    }
}
