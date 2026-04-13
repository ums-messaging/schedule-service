package com.ums.schedule.attachment.fixture.builder;

import com.ums.schedule.adapter.api.send.email.EmailSecurityPolicyRequest;
import com.ums.schedule.adapter.api.send.email.EmailSendCreateRequest;

import java.util.UUID;

public class EmailMessageCommandBuilder {
    private String templateKey = UUID.randomUUID().toString();
    private String convertType = "NONE";
    private String encodingType;
    private EmailSecurityPolicyRequest securityPolicy;

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

    public EmailMessageCommandBuilder securityPolicy(EmailSecurityPolicyRequest securityPolicy) {
        this.securityPolicy = securityPolicy;
        return this;
    }

    public EmailSendCreateRequest build() {
        return new EmailSendCreateRequest(
                null,
                this.convertType,
                this.encodingType,
                this.securityPolicy
        );
    }
}
