package com.ums.schedule.adapter.api.send.email;

public record EmailSecurityPolicyRequest(
        String encryptionType,
        String passwordPolicy,
        String passwordHash,
        String passwordFormat,
        String permissionMask
) {
}
