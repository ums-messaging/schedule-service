package com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity;

import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.orchestrator.domain.event.SendRequest;
import com.ums.schedule.orchestrator.domain.event.customer.CustomerRequestKey;
import com.ums.schedule.orchestrator.domain.event.state.SendRequestState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Entity
@Table(name = "send_request", uniqueConstraints = {
            @UniqueConstraint(
                    name="uq_customer_request",
                    columnNames = {"customer_id", "customer_key"}
            )})
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendRequestEntity implements Persistable<Long> {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount;

    @Column(name = "channel_type", nullable = false)
    private String channelType;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "customer_id", nullable = false)
    private String customerId;
    @Column(name = "customer_key", nullable = false)
    private String customerKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_id", nullable = false)
    private SendRequestUploadEntity sendRequestUpload;

    private LocalDateTime createdAt;
    private LocalDateTime requestedAt;

    @Transient
    private boolean isNew;

    public static SendRequestEntity fromDomain(SendRequest sendRequest) {
        SendRequestEntity entity = new SendRequestEntity(sendRequest);
        entity.assignCustomerInfo(sendRequest.getCustomerRequestKey());
        return entity;
    }

    private SendRequestEntity(SendRequest domain) {
        this.id = domain.getId();
        this.retryCount = domain.getRetryCount();
        this.channelType = domain.getChannelType().code();
        this.status = domain.getState().getCurrentCode().code();
        this.createdAt = domain.getCreatedAt();
        this.requestedAt = domain.getRequestedAt();
    }
    private void assignCustomerInfo(CustomerRequestKey info) {
        this.customerId = info.getCustomerId();
        this.customerKey = info.getCustomerkey();
    }

    public void assignSchedule(ScheduleEntity schedule) {
        this.schedule = schedule;
    }

    public void assignSendRequestUpload(SendRequestUploadEntity sendRequestUpload) {
        this.sendRequestUpload = sendRequestUpload;
    }

    public SendRequest toDomain() {
        return SendRequest.fromEntity(this);
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @PrePersist
    public void prePersist() {
        if(createdAt == null) {
            this.createdAt = LocalDateTime.now();
            this.isNew = true;
        }
    }
    @PostLoad
    public void postLoad() {
        this.isNew = false;
    }

    public void updateStatus(SendRequest sendRequest) {
        this.status = sendRequest.getState().getCurrentCode().code();
        this.requestedAt = sendRequest.getRequestedAt();
    }
}