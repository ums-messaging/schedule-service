package com.ums.schedule.domain.request.event;

import com.ums.schedule.ResultCodeEnum;
import com.ums.schedule.SendRequestEventEnum;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.code.send.ResultCodeEnum;
import com.ums.schedule.code.send.SendRequestEventEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.status.RequestCreateState;
import com.ums.schedule.domain.request.status.SendRequestState;
import com.ums.schedule.domain.request.exception.status.SendRequestStatusException;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class SendRequestEvent {
    private String eventId;
    private SendRequestEventEnum eventType;
    private ResultCodeEnum resultCode;
    private String resultMessage;

    @ManyToOne
    private SendRequest sendRequest;

    private LocalDateTime issuedAt;

    public static SendRequestEvent of(SendRequest sendRequest, EnumMapperValue eventType) {
        SendRequestEvent event = new SendRequestEvent(eventType);
        event.applySendRequest(sendRequest);
        return event;
    }

    private SendRequestEvent(EnumMapperValue eventType) {
        this.eventType = SendRequestEventEnum.valueOf(eventType.code());
        this.issuedAt = LocalDateTime.now();
    }

    public SendRequestState mark(SendRequestState state) {
        try {
            this.eventType = SendRequestEventEnum.valueOf(eventType.code());
            SendRequestState getState = getStateOnEvent(state);
            setResult(ResultCodeEnum.SUCCESS);
            return getState;
        } catch (SendRequestStatusException e) {
            setError(e);
            return state;
        }
    }

    private SendRequestState getStateOnEvent(SendRequestState state) {
        SendRequestState toState = (state == null) ?
                new RequestCreateState() : state.onEvent(this);
        return toState;
    }

    private void applySendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        this.sendRequest.addEventList(this);
    }

    public void setError(SendRequestStatusException e) {
        setResult(ResultCodeEnum.FAIL, e.getMessage());
    }

    public void setResult(ResultCodeEnum resultCode) {
        setResult(resultCode, resultCode.description());
    }

    public void setResult(ResultCodeEnum resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

}
