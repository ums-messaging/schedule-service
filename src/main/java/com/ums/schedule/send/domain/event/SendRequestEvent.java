package com.ums.schedule.send.domain.event;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.send.code.ResultCodeEnum;
import com.ums.schedule.send.code.SendRequestEventEnum;
import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.exception.SendRequestException;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.status.RequestCreateState;
import com.ums.schedule.send.domain.request.status.SendRequestState;
import com.ums.schedule.send.domain.request.status.exception.SendStatusException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.ums.schedule.send.code.SendRequestEventEnum.MESSAGE_CREATED;
import static com.ums.schedule.send.code.SendRequestEventEnum.TARGET_UPLOADED;

@Getter
public class SendRequestEvent {
    private String eventId;
    private SendRequestEventEnum eventType;
    private ResultCodeEnum resultCode;
    private String resultMessage;
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
        } catch (SendStatusException e) {
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

    public void setError(SendRequestException e) {
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
