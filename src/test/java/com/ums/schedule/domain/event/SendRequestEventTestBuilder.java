package com.ums.schedule.domain.event;

import com.ums.schedule.domain.send.group.SendGroupEvent;
import com.ums.schedule.domain.send.code.ResultCodeEnum;
import com.ums.schedule.domain.sendrequest.code.SendGroupEventTypeEnum;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.SendRequestTestBuilder;

import java.time.LocalDateTime;

public class SendRequestEventTestBuilder {
    private SendGroupEventTypeEnum eventType = SendGroupEventTypeEnum.SEND_GROUP_CREATED;
    private ResultCodeEnum resultCode = ResultCodeEnum.SUCCESS;
    private String resultMessage;
    private SendRequest sendRequest = SendRequestTestBuilder.builder().build();
    private String payload;
    private LocalDateTime issuedAt=LocalDateTime.now();

    public static SendRequestEventTestBuilder builder() {
        return new SendRequestEventTestBuilder();
    }


    public SendRequestEventTestBuilder eventType(SendGroupEventTypeEnum eventType) {
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

    public SendGroupEvent build() {
        return new SendGroupEvent(
                null,
                this.eventType,
                this.resultCode,
                this.resultMessage,
                this.sendRequest,
                this.payload,
                issuedAt
        );
    }
}
