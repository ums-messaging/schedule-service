package com.ums.schedule.adapter.api.sendrequest.email.request;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;

import java.util.UUID;

public class SendRequestCreateRequestBuilder {
    private Long scheduleId = 1L;
    private String customerRequestId = UUID.randomUUID().toString();
    private String uploadFormat;
    private Integer retryCount = 3;
    private String messageType = null;
    private String senderKey = "test@test.com";
    private String templateKey = UUID.randomUUID().toString();

    public static SendRequestCreateRequestBuilder builder() {
        return new SendRequestCreateRequestBuilder();
    }

    public SendRequestCreateRequestBuilder scheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
        return this;
    }

    public SendRequestCreateRequest build() {
        return new SendRequestCreateRequest(
                scheduleId,
                customerRequestId,
                uploadFormat,
                senderKey,
                templateKey,
                messageType,
                retryCount
        );
    }
}
