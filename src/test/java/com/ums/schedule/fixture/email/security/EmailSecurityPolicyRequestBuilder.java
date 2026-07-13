package com.ums.schedule.fixture.email.security;

import com.ums.schedule.adapter.api.request.email.EmailSecurityPolicyRequest;

public class EmailSecurityPolicyRequestBuilder {
    private String encryptionType;
    private String passwordPolicy;
    private String passwordHash;
    private String passwordFormat;
    private String permissionMask;

    public static EmailSecurityPolicyRequestBuilder builder() {
        return new EmailSecurityPolicyRequestBuilder();
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
