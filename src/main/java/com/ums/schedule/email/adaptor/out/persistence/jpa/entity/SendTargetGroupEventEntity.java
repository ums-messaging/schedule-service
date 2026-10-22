package com.ums.schedule.email.adaptor.out.persistence.jpa.entity;

import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.email.domain.event.SendTargetGroupEvent;
import com.ums.schedule.email.domain.target.group.DomainGroup;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "send_target_group_event", uniqueConstraints = {
        @UniqueConstraint(
                name="uq_group_event",
                columnNames = { "group_id", "event_type" }
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SendTargetGroupEventEntity {
    @Id
    @Tsid
    private Long id;

    @Column(nullable = false)
    private Long sendRequestId;
    @Column(nullable = false)
    private Long groupId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "result_code", nullable = false)
    private String resultCode;
    private String resultMessage;

    private Integer totalCount;
    private Integer successCount;
    private Integer failCount;

    private LocalDateTime issuedAt;


    public static SendTargetGroupEventEntity fromDomain(SendTargetGroupEvent domain) {
        SendTargetGroupEventEntity entity = new SendTargetGroupEventEntity(domain);
        return entity;
    }

    private SendTargetGroupEventEntity(SendTargetGroupEvent event) {
        this.groupId = event.getDomainGroup().groupId();
        this.eventType = event.getEventType().code();
        this.sendRequestId = event.getSendRequestId();
        this.resultCode = event.getResultCode().code();
        this.resultMessage = event.getResultMessage();
        this.totalCount = event.getTotalCount();
        this.successCount = event.getSuccessList().size();
        this.failCount = event.getErrorList().size();
        this.issuedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist(){
        this.issuedAt = LocalDateTime.now();
    }
}