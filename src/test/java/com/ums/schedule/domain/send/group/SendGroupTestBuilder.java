package com.ums.schedule.domain.send.group;

import com.ums.schedule.domain.send.code.ResultCodeEnum;
import com.ums.schedule.domain.sendrequest.code.SendGroupEventTypeEnum;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.SendRequestTestBuilder;

import java.time.LocalDateTime;

public class SendGroupTestBuilder {
    private SendGroupEventTypeEnum eventType = SendGroupEventTypeEnum.SEND_GROUP_CREATED;
    private ResultCodeEnum resultCode = ResultCodeEnum.SUCCESS;
    private String resultMessage;
    private SendRequest sendRequest = SendRequestTestBuilder.builder().build();
    private String payload;
    private LocalDateTime issuedAt=LocalDateTime.now();

    public static SendGroupTestBuilder builder() {
        return new SendGroupTestBuilder();
    }


    public SendGroupTestBuilder eventType(SendGroupEventTypeEnum eventType) {
        this.eventType = eventType;
        return this;
    }

    public SendGroupTestBuilder resultCode(ResultCodeEnum resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public SendGroupTestBuilder sendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        return this;
    }

    public SendGroupTestBuilder payload(String payload) {
        this.payload = payload;
        return this;
    }

    public SendGroupTestBuilder issuedAt(LocalDateTime issuedAt) {
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
