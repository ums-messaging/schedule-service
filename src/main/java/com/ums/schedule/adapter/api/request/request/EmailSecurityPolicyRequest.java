package com.ums.schedule.adapter.api.request.request;

import com.ums.schedule.domain.sendrequest.resource.email.policy.SecurityPolicy;

public record EmailSecurityPolicyRequest(
        String encryptionType,
        String passwordPolicy,
        String passwordHash,
        String passwordFormat,
        String permissionMask
) {
    public static EmailSecurityPolicyRequest of(SecurityPolicy securityPolicy) {
        return new EmailSecurityPolicyRequest(
                securityPolicy.getEncryptionType().value(),
                securityPolicy.getPasswordPolicy(),
                securityPolicy.getPasswordHash().value(),
                securityPolicy.getPasswordFormat(),
                securityPolicy.getPermissionMask().value()
        );
    }
}
