package com.ums.schedule.application.sendrequest.command;

public record SendRequestUpdateCommand(
    String templateKey,
    String senderKey,
    Integer retryCount
) {
}
