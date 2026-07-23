package com.ums.schedule.domain.send.group;

import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.common.code.email.EmailResultCode;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ums.schedule.common.code.email.EmailResultCode.FAIL;

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
    private SendGroupEventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_code", nullable = false)
    private EmailResultCode resultCode;

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


    private void assignSendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
    }

    private SendGroupEvent(SendGroupEventType eventType) {
        this.eventType = eventType;
        this.resultCode = EmailResultCode.SUCCESS;
    }

    private void onError(SendRequest sendRequest, String message) {
        setResult(FAIL, message);
    }


    private void setResult(EmailResultCode code, String message) {
        this.resultCode = code;
        this.resultMessage = message;
    }

    @PrePersist
    public void prePersist(){
        this.issuedAt = LocalDateTime.now();
    }
}