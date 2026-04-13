package com.ums.schedule.attachment.fixture.builder;

import com.ums.schedule.adapter.api.send.email.EmailSecurityPolicyRequest;

public class SecurityPolicyCommandBuilder {
    private String encryptionType = "ASE-256";
    private String passwordPolicy = "BIRTHDAY";
    private String passwordHash = "SHA-256";
    private String passwordFormat;
    private String permissionMask = "ALL";

    private SecurityPolicyCommandBuilder() {
    }

    public static SecurityPolicyCommandBuilder builder() {
        return new SecurityPolicyCommandBuilder();
    }

    public EmailSecurityPolicyRequest build() {
        return new EmailSecurityPolicyRequest(
            encryptionType,
            passwordPolicy,
            passwordHash,
            passwordFormat,
            permissionMask
        );
    }

}
