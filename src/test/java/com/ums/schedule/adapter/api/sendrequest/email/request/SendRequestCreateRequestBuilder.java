package com.ums.schedule.adapter.api.sendrequest.email.request;

import com.ums.schedule.adapter.api.request.request.SendRequestCreateRequest;

import java.util.UUID;

public class SendRequestCreateRequestBuilder {
    private String customerRequestId = UUID.randomUUID().toString();
    private Integer retryCount = 3;
    private Long scheduleId = 1L;
    private String messageType = null;
    private String senderKey = "test@test.com";
    private String templateKey = UUID.randomUUID().toString();

    public static SendRequestCreateRequestBuilder builder() {
        return new SendRequestCreateRequestBuilder();
    }

    public SendRequestCreateRequest build() {
        return new SendRequestCreateRequest(
                scheduleId,
                customerRequestId,
                senderKey,
                templateKey,
                messageType,
                retryCount,
                null,
                null
        );
    }
}
