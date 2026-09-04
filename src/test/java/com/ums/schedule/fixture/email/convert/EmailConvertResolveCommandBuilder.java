package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.generator.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.fixture.email.attachment.AttachmentContextBuilder;

public class EmailConvertResolveCommandBuilder {
    private String convertType;
    private AttachmentContext body;
    private AttachmentContext cover;

    public static EmailConvertResolveCommandBuilder builder() {
        return new EmailConvertResolveCommandBuilder();
    }

    public EmailConvertResolveCommandBuilder convertType(String convertType) {
        this.convertType = convertType;
        return this;
    }

    public EmailConvertResolveCommandBuilder body(String bodyKey) {
        this.body = givenContext(bodyKey);
        return this;
    }

    public EmailConvertResolveCommandBuilder cover(String coverKey) {
        this.cover = givenContext(coverKey);
        return this;
    }

    public EmailConvertResolveCommand build() {
        return new EmailConvertResolveCommand(
                convertType,
                body,
                cover
        );
    }

    private AttachmentContext givenContext(String fileKey) {
        return AttachmentContextBuilder.builder()
                .key(fileKey)
                .build();
    }
}
