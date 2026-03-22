package com.ums.schedule.message.application.command;

import com.ums.schedule.attachment.application.command.SecurityPolicyCommand;

public record EmailMessageCommand(
        String templateKey,
        String convertType,
        String encodingType,
        SecurityPolicyCommand securityPolicy
) {
}
