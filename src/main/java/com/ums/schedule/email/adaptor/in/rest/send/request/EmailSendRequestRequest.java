package com.ums.schedule.email.adaptor.in.rest.send.request;


public record EmailSendRequestRequest(
        Long requestId,
        String messageId,
        String senderKey
) {

}
