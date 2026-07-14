package com.ums.schedule.domain.send.group;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.send.code.ResultCodeEnum;
import com.ums.schedule.common.code.request.SendGroupEventTypeEnum;
import com.ums.schedule.domain.exception.request.SendRequestException;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ums.schedule.domain.send.code.ResultCodeEnum.FAIL;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SendGroupEvent {
    @Id
    @Tsid
    private Long eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private SendGroupEventTypeEnum eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_code", nullable = false)
    private ResultCodeEnum resultCode;

    private String resultMessage;

    @JoinColumn(name = "request_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SendRequest sendRequest;

    private String payload;

    private LocalDateTime issuedAt;

    public static SendGroupEvent of(SendRequest sendRequest) {
        SendGroupEvent event = new SendGroupEvent();
        event.assignSendRequest(sendRequest);
        return event;
    }

    public static SendGroupEvent of(SendRequest sendRequest, SendRequestException e) {
        SendGroupEvent event = SendGroupEvent.of(sendRequest);
        event.onError(sendRequest, e);
        return event;
    }

    private void assignSendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
    }

    private SendGroupEvent(SendGroupEventTypeEnum eventType) {
        this.eventType = eventType;
        this.resultCode = ResultCodeEnum.SUCCESS;
    }

    private void onError(SendRequest sendRequest, SendRequestException e) {
        setResult(FAIL, e.getMessage());
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