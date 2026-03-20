package com.ums.schedule.attachment.application.command;

public record SecurityPolicyCommand(
        String encryptionType,
        String passwordPolicy,
        String passwordHash,
        String passwordFormat,
        String permissionMask
) {
}
