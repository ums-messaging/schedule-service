package com.ums.schedule.adapter.api.sendrequest.email.request;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;

import java.util.UUID;

public class SendRequestCreateRequestBuilder {
    private Long scheduleId = 1L;
    private String customerRequestId = UUID.randomUUID().toString();
    private String uploadFormat;
    private Integer retryCount = 3;
    private String messageType;
    private String senderKey;
    private String templateKey;

    public static SendRequestCreateRequestBuilder builder() {
        return new SendRequestCreateRequestBuilder();
    }

    private SendRequestCreateRequestBuilder() {
        this.scheduleId = 1L;
        this.senderKey = "test@test.com";
        this.templateKey = "my_template";
        this.customerRequestId = "my_send_request";
        this.uploadFormat = "csv";
        this.messageType = "AD";
        this.retryCount = 3;
    }

    public SendRequestCreateRequestBuilder scheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
        return this;
    }

    public SendRequestCreateRequestBuilder templateKey(String templateKey) {
        this.templateKey = templateKey;
        return this;
    }

    public SendRequestCreateRequestBuilder customerRequestId(String customerRequestId) {
        this.customerRequestId = customerRequestId;
        return this;
    }

    public SendRequestCreateRequestBuilder uploadFormat(String uploadFormat) {
        this.uploadFormat = uploadFormat;
        return this;
    }

    public SendRequestCreateRequestBuilder messageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public SendRequestCreateRequestBuilder retryCount(Integer retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public SendRequestCreateRequestBuilder senderKey(String senderKey) {
        this.senderKey = senderKey;
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
