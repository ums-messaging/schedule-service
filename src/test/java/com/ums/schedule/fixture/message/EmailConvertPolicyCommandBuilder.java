package com.ums.schedule.fixture.message;

import com.ums.schedule.application.ums.email.convert.resolver.model.AttachmentResolveCommand;
import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;

import java.util.List;

public class EmailConvertPolicyCommandBuilder {
    private String convertType;
    private String header;
    private String body;
    private String cover;
    private String footer;
    private List<AttachmentResolveCommand> attachmentList;

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

    public EmailConvertPolicyCommandBuilder header(String header) {
        this.header = header;
        return this;
    }

    public EmailConvertPolicyCommandBuilder body(String body) {
        this.body = body;
        return this;
    }

    public EmailConvertPolicyCommandBuilder cover(String cover) {
        this.cover = cover;
        return this;
    }

    public EmailConvertPolicyCommandBuilder footer(String footer) {
        this.footer = footer;
        return this;
    }

    public EmailConvertPolicyCommandBuilder attachmentList(List<AttachmentResolveCommand> attachmentList) {
        this.attachmentList = attachmentList;
        return this;
    }

    public EmailConvertResolveCommand build() {
        return new EmailConvertResolveCommand(
                convertType,
                header,
                body,
                cover,
                footer,
                attachmentList
        );
    }
}
