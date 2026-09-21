package com.ums.schedule.domain.send.group;

import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.domain.send.email.job.DomainGroup;
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
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name="uq_group_event",
                columnNames = {"group_id", "event_type", "result_code"}
        )
}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SendGroupEvent {
    @Id
    @Tsid
    private Long eventId;

    @Column(nullable = false)
    private Long groupId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private SendGroupEventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_code", nullable = false)
    private EmailResultCode resultCode;
    private String resultMessage;

    private Long totalCount;
    private Long successCount;
    private Long failCount;

    private Long sendRequestId;

    private String payload;

    private LocalDateTime issuedAt;

    public static SendGroupEvent of(SendGroupEventType eventType, DomainGroup domainGroup, EmailResultCode resultCode, String resultMessage) {
        SendGroupEvent event = new SendGroupEvent(domainGroup, eventType);
        event.setResult(resultCode, resultMessage);
        return event;
    }

    private SendGroupEvent(DomainGroup domainGroup, SendGroupEventType eventType) {
        this.sendRequestId = Long.parseLong(domainGroup.requestId());
        this.eventType = eventType;
        this.groupId = domainGroup.groupId();
        this.totalCount = domainGroup.totalCount();
        this.payload = JsonUtil.toJson(domainGroup);
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