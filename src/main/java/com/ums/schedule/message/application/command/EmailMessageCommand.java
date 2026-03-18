package com.ums.schedule.message.application.command;

public record EmailMessageCommand(
        String templateId,
        String convertType,
        String contentType,
        String encodingType
) {
}
