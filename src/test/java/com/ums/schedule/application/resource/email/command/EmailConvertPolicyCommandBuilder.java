package com.ums.schedule.application.resource.email.command;

import com.ums.schedule.application.sendrequest.message.email.command.EmailConvertPolicyCommand;
import com.ums.schedule.application.template.email.query.model.EmailTemplateContentResult;
import com.ums.schedule.fixture.template.EmailContentResultBuilder;

import java.util.List;

public class EmailConvertPolicyCommandBuilder {
    private String convertType;
    private boolean hasSecurityPolicy;
    private String encryptionType;
    private String passwordHash;
    private String passwordFormat;
    private String passwordPolicy;
    private String permissionMask;
    private EmailTemplateContentResult header;
    private EmailTemplateContentResult body;
    private EmailTemplateContentResult cover;
    private EmailTemplateContentResult footer;
    private List<EmailTemplateContentResult> attachmentList;

    public static EmailConvertPolicyCommandBuilder builder() {
        return new EmailConvertPolicyCommandBuilder();
    }

    private EmailConvertPolicyCommandBuilder() {
        this.hasSecurityPolicy = false;
        this.body = EmailContentResultBuilder.builder().body().build();
        this.attachmentList = List.of();
    }

    public EmailConvertPolicyCommandBuilder convertType(String convertType) {
        this.convertType = convertType;
        return this;
    }

    public EmailConvertPolicyCommandBuilder hasSecurityPolicy(boolean hasSecurityPolicy) {
        this.hasSecurityPolicy = hasSecurityPolicy;
        return this;
    }

    public EmailConvertPolicyCommandBuilder encryptionType(String encryptionType) {
        this.encryptionType = encryptionType;
        return this;
    }

    public EmailConvertPolicyCommandBuilder passwordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public EmailConvertPolicyCommandBuilder passwordFormat(String passwordFormat) {
        this.passwordFormat = passwordFormat;
        return this;
    }

    public EmailConvertPolicyCommandBuilder passwordPolicy(String passwordPolicy) {
        this.passwordPolicy = passwordPolicy;
        return this;
    }

    public EmailConvertPolicyCommandBuilder permissionMask(String permissionMask) {
        this.permissionMask = permissionMask;
        return this;
    }

    public EmailConvertPolicyCommandBuilder header(EmailTemplateContentResult header) {
        this.header = header;
        return this;
    }

    public EmailConvertPolicyCommandBuilder body(EmailTemplateContentResult body) {
        this.body = body;
        return this;
    }

    public EmailConvertPolicyCommandBuilder cover(EmailTemplateContentResult cover) {
        this.cover = cover;
        return this;
    }

    public EmailConvertPolicyCommandBuilder footer(EmailTemplateContentResult footer) {
        this.footer = footer;
        return this;
    }

    public EmailConvertPolicyCommandBuilder attachmentList(List<EmailTemplateContentResult> attachmentList) {
        this.attachmentList = attachmentList;
        return this;
    }

    public EmailConvertPolicyCommand build() {
        return new EmailConvertPolicyCommand(
                convertType,
                hasSecurityPolicy,
                encryptionType,
                passwordHash,
                passwordFormat,
                passwordPolicy,
                permissionMask,
                header,
                body,
                cover,
                footer,
                attachmentList
        );
    }
}
