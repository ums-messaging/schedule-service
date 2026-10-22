package com.ums.schedule.email.application.requests.result;


import com.ums.schedule.email.domain.requests.EmailSendRequest;

public record SendRequestSaveResult(
        Long sendRequestId,
        Long requestId,
        String from,
        String fromName
) {
    public static SendRequestSaveResult of(EmailSendRequest sendRequest) {
        return new SendRequestSaveResult(
                sendRequest.getId(),
                sendRequest.getId(),
                sendRequest.getSender().from(),
                sendRequest.getSender().fromName()
        );
    }
}
