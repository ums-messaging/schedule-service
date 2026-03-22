package com.ums.schedule.attachment.fixture.builder;

import com.ums.schedule.attachment.application.command.SecurityPolicyCommand;
import com.ums.schedule.message.application.command.EmailMessageCommand;

import java.util.UUID;

public class EmailMessageCommandBuilder {
    private String templateKey = UUID.randomUUID().toString();
    private String convertType = "NONE";
    private String encodingType;
    private SecurityPolicyCommand securityPolicy;

    private EmailMessageCommandBuilder() { }

    public static EmailMessageCommandBuilder builder() {
        return new EmailMessageCommandBuilder();
    }

    public EmailMessageCommandBuilder convertType(String convertType) {
        this.convertType = convertType;
        return this;
    }

    public EmailMessageCommandBuilder encodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    public EmailMessageCommandBuilder securityPolicy(SecurityPolicyCommand securityPolicy) {
        this.securityPolicy = securityPolicy;
        return this;
    }

    public EmailMessageCommand build() {
        return new EmailMessageCommand(
                this.templateKey,
                this.convertType,
                this.encodingType,
                this.securityPolicy
        );
    }
}
