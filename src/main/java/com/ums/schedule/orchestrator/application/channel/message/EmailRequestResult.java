package com.ums.schedule.orchestrator.application.channel.message;

import com.ums.schedule.email.domain.requests.EmailSendRequest;

public record EmailRequestResult(
        Long requestId
) implements ChannelRequestResult {
    public static EmailRequestResult of(EmailSendRequest sendRequest) {
        return new EmailRequestResult(
                sendRequest.getId()
        );
    }
}
