package com.ums.schedule.domain.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ums.schedule.application.request.dto.SendRequestCommand;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.code.send.ResultCodeEnum;
import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.request.exception.SendRequestException;
import com.ums.schedule.domain.request.exception.state.SendRequestStateException;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.upload.TargetUpload;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ums.schedule.code.send.ResultCodeEnum.FAIL;
import static com.ums.schedule.code.send.SendRequestEventTypeEnum.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SendRequestEvent {
    @Id
    @Tsid
    private Long eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private SendRequestEventTypeEnum eventType;
    @Enumerated(EnumType.STRING)
    @Column(name = "result_code", nullable = false)
    private ResultCodeEnum resultCode;
    private String resultMessage;

    @JoinColumn(name = "request_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SendRequest sendRequest;

    @Transient
    private SendEvent event;
    private String payload;

    private LocalDateTime issuedAt;

    public static SendRequestEvent of(SendRequest sendRequest, SendEvent sendEvent) {
        SendRequestEvent event = new SendRequestEvent(sendEvent.getEventType());
        sendRequest.onEvent(event);
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
            this.sendRequest = sendRequest;
        } catch (SendRequestStateException e) {
            onError(sendRequest, e);
        }
    }

    private SendRequestEvent(SendRequestEventTypeEnum eventType) {
        this.eventType = eventType;
        this.resultCode = ResultCodeEnum.SUCCESS;
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

    @PrePersist
    public void prePersist(){
        this.issuedAt = LocalDateTime.now();
    }
}