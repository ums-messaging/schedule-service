package com.ums.schedule.email.application.requests.command;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.email.adaptor.in.rest.send.request.EmailSendRequestRequest;
import com.ums.schedule.email.domain.requests.sender.Sender;

import java.util.UUID;

public record SendRequestCommand(
        Long requestId,
        UUID messageId,
        Sender sender
) {
    public static SendRequestCommand of(EmailSendRequestRequest request, Sender sender) {
        return new SendRequestCommand(
                request.requestId(),
                UuidCreator.fromString(request.messageId()),
                sender
        );
    }
}
