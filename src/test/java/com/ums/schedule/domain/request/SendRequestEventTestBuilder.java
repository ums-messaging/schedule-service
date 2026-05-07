package com.ums.schedule.domain.request;

import com.ums.schedule.code.send.ResultCodeEnum;
import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.domain.request.event.SendEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class SendRequestEventTestBuilder {
    private SendRequestEventTypeEnum eventType = SendRequestEventTypeEnum.REQUEST_CREATED;
    private ResultCodeEnum resultCode = ResultCodeEnum.SUCCESS;
    private String resultMessage;
    private SendRequest sendRequest = SendRequestTestBuilder.builder().build();
    private SendEvent event;
    private String payload;
    private LocalDateTime issuedAt=LocalDateTime.now();

    public static SendRequestEventTestBuilder builder() {
        return new SendRequestEventTestBuilder();
    }


    public SendRequestEventTestBuilder eventType(SendRequestEventTypeEnum eventType) {
        this.eventType = eventType;
        return this;
    }

    public SendRequestEventTestBuilder resultCode(ResultCodeEnum resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public SendRequestEventTestBuilder sendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        return this;
    }

    public SendRequestEventTestBuilder payload(String payload) {
        this.payload = payload;
        return this;
    }

    public SendRequestEventTestBuilder issuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }

    public SendRequestEvent build() {
        return new SendRequestEvent(
                null,
                this.eventType,
                this.resultCode,
                this.resultMessage,
                this.sendRequest,
                null,
                this.payload,
                issuedAt
        );
    }
}
