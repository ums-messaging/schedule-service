package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;

import java.util.List;

public class EmailConvertPolicyCommandBuilder {
    private String convertType;
    private AttachmentContext body;
    private AttachmentContext cover;
    private List<AttachmentContext> attachmentList;

    public static EmailConvertPolicyCommandBuilder builder() {
        return new EmailConvertPolicyCommandBuilder();
    }

    private EmailConvertPolicyCommandBuilder() {
        this.attachmentList = List.of();
    }

    public EmailConvertPolicyCommandBuilder convertType(String convertType) {
        this.convertType = convertType;
        return this;
    }

    public EmailConvertPolicyCommandBuilder body(AttachmentContext body) {
        this.body = body;
        return this;
    }

    public EmailConvertPolicyCommandBuilder cover(AttachmentContext cover) {
        this.cover = cover;
        return this;
    }

    public EmailConvertPolicyCommandBuilder attachmentList(List<AttachmentContext> attachmentList) {
        this.attachmentList = attachmentList;
        return this;
    }

    public EmailConvertResolveCommand build() {
        return new EmailConvertResolveCommand(
                convertType,
                body,
                cover
        );
    }
}
