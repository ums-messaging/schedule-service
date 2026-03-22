package com.ums.schedule.attachment.fixture.builder;

import com.ums.schedule.attachment.application.command.SecurityPolicyCommand;

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

    public SecurityPolicyCommand build() {
        return new SecurityPolicyCommand(
            encryptionType,
            passwordPolicy,
            passwordHash,
            passwordFormat,
            permissionMask
        );
    }

}
