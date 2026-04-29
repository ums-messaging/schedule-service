package com.ums.schedule.domain.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.code.send.ResultCodeEnum;
import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.request.event.SendRequestedEvent;
import com.ums.schedule.domain.request.exception.SendRequestException;
import com.ums.schedule.domain.request.exception.state.SendRequestStateException;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.target.upload.TargetUpload;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ums.schedule.code.send.ResultCodeEnum.FAIL;
import static com.ums.schedule.code.send.SendRequestEventTypeEnum.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendRequestEvent {
    @Id
    private String eventId;

    private SendRequestEventTypeEnum eventType;
    private ResultCodeEnum resultCode;
    private String resultMessage;

    @ManyToOne
    private SendRequest sendRequest;

    @Transient
    private SendEvent event;
    private String payload;

    private LocalDateTime issuedAt;

    public static SendRequestEvent of(Schedule schedule, CustomerRequestKey customerKey, ChannelTypeEnum channelType) {
        SendRequestEvent requestEvent = new SendRequestEvent(REQUEST_CREATED);
        SendRequest request = SendRequest.of(schedule, customerKey, channelType);
        requestEvent.applySendRequest(request);
        return requestEvent;
    }

    public static SendRequestEvent of(SendRequest sendRequest, SendEvent sendEvent) {
        SendRequestEvent event = new SendRequestEvent(sendEvent.getEventType());
        event.applySendEvent(sendEvent);
        event.applySendRequest(sendRequest);
        return event;
    }

    public static SendRequestEvent of(SendRequest sendRequest, SendEvent sendEvent, SendRequestException e) {
        SendRequestEvent event = SendRequestEvent.of(sendRequest, sendEvent);
        event.onError(sendRequest, e);
        return event;
    }

    private void applySendRequest(SendRequest sendRequest) {
        try {
            this.sendRequest = sendRequest.onEvent(this);
        } catch (SendRequestStateException e) {
            onError(sendRequest, e);
        }
    }

    private SendRequestEvent(SendRequestEventTypeEnum eventType) {
        this.eventType = eventType;
        this.resultCode = ResultCodeEnum.SUCCESS;
        this.issuedAt = LocalDateTime.now();
    }

    private void onError(SendRequest sendRequest, SendRequestException e) {
        setResult(FAIL, e.getMessage());
        this.sendRequest = sendRequest.onError();
    }

    private void applySendEvent(SendEvent event) {
        this.event = event;
        toPayload(event);
    }

    public String toPayload(SendEvent event) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.payload = event.toJson(mapper);
        } catch (JsonProcessingException e) {
            this.payload = e.getMessage();
        }
        return this.payload;
    }

    private void setResult(ResultCodeEnum code, String message) {
        this.resultCode = code;
        this.resultMessage = message;
    }
}