package com.ums.schedule.message.application.command;

import com.ums.schedule.attachment.application.command.SecurityPolicyCommand;
import com.ums.schedule.send.application.model.command.SendRequestCommand;

public record EmailMessageCommand(
        SendRequestCommand sendRequest,
        String convertType,
        String encodingType,
        SecurityPolicyCommand securityPolicy
) {
}
