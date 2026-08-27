package com.ums.schedule.domain.request;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.application.sendrequest.command.SendRequestUpdateCommand;
import com.ums.schedule.application.ums.common.request.model.SendRequestCreateContext;
import com.ums.schedule.common.code.api.SendRequestErrorCode;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.common.util.FileUtil;
import com.ums.schedule.common.code.request.SendRequestEvent;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.domain.request.converter.ChannelTypeConverter;
import com.ums.schedule.domain.request.customer.CustomerRequestKey;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.exception.SendRequestDomainException;
import com.ums.schedule.domain.request.state.SendRequestCreateState;
import com.ums.schedule.domain.request.state.SendRequestState;
import com.ums.schedule.domain.request.converter.SendRequestStateConverter;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.schedule.Schedule;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "send_request", uniqueConstraints = {
            @UniqueConstraint(
                    name="uq_customer_request",
                    columnNames = {"customer_id", "customer_request_id"}
            )})
@Getter
@AllArgsConstructor
public class SendRequest implements Persistable<Long> {
    @Id
    @Column(name = "send_request_id")
    private Long id;

    @Column(name = "retry_cnt", nullable = false)
    private Integer retryCnt;

    @Column(name = "sender_key", nullable = false)
    private String senderKey;

    @Column(name = "template_key", nullable = false)
    private String templateKey;

    @Column(name = "channel_type", nullable = false)
    @Convert(converter = ChannelTypeConverter.class)
    private ChannelType channelType;

    @Convert(converter = SendRequestStateConverter.class)
    @Column(name = "status", nullable = false)
    private SendRequestState state;

    @Transient
    private SendRequestEvent event;

    @Embedded
    private CustomerRequestKey customerRequestKey;

    @JoinColumn(name = "current_upload_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private TargetUploadReport currentTargetUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private SendMessage sendMessage;

    private LocalDateTime createdAt;
    private LocalDateTime requestedAt;
    private LocalDateTime sendStartedAt;
    private LocalDateTime sendCompletedAt;

    @Transient
    private boolean isNew;
    public static SendRequest of(SendRequestCreateContext context) {
        SendRequest request = new SendRequest();
        request.assignChannelType(context.channelType());
        request.assignCustomerKey(context.customerKey());
        request.assignSchedule(context.schedule());
        request.assignSendMessage(context.sendMessage());
        request.assignSenderAndTemplate(context.senderKey(), context.templateKey());
        request.initializeRetryCount(context.retryCnt());
        request.initializeStatusAndEvent();
        request.initializeCreateAt();
        return request;
    }

    private void assignSendMessage(SendMessage sendMessage) {
        this.sendMessage = Objects.requireNonNull(sendMessage);
    }

    private void assignChannelType(ChannelType channelType) {
        this.channelType = Objects.requireNonNull(channelType, "channel_type");
    }

    protected SendRequest() {
        this.id = TsidCreator.getTsid().toLong();
        this.isNew = true;
    }

    private void initializeStatusAndEvent() {
        this.event = SendRequestEvent.SEND_REQUEST_CREATED;
        changeStatus(new SendRequestCreateState());
    }
    private void assignSenderAndTemplate(String senderKey, String templateKey) {
        assignSender(senderKey);
        assignTemplate(templateKey);
    }
    private void assignSender(String senderKey) {
        this.senderKey = Objects.requireNonNull(senderKey, "sender_key");
    }


    private void assignTemplate(String templateKey) {
        this.templateKey = Objects.requireNonNull(templateKey, "template_key");
    }

    private void initializeRetryCount(Integer retryCount) {
        this.retryCnt = retryCount;
    }

    private void initializeCreateAt() {
        this.createdAt = LocalDateTime.now();
    }

    private void assignSchedule(Schedule schedule) {
        this.schedule = Objects.requireNonNull(schedule, "schedule");
        schedule.checkScheduleAvailability();
    }

    private void assignCustomerKey(CustomerRequestKey customerRequestKey) {
        this.customerRequestKey = Optional.ofNullable(customerRequestKey)
                .orElseThrow(() -> SendRequestDomainException.of(SendRequestErrorCode.DUPLICATED_CUSTOMER_KEY));
    }

    public SendRequest updateSendRequest(Schedule schedule, TargetUploadReport targetUpload, SendRequestUpdateCommand command) {
        updateStateByTargetUploadReport(targetUpload);
        assignSchedule(Optional.ofNullable(schedule).orElse(this.schedule));
        updateStateByTargetUploadReport(targetUpload);
        assignSenderAndTemplate(command.senderKey(), command.templateKey());
        initializeRetryCount(command.retryCount());
        return this;
    }

    public void updateStateByTargetUploadReport(TargetUploadReport targetUpload) {
        onEvent(SendRequestEvent.SEND_REQUEST_UPDATED);
        if(targetUpload != null) {
            if(targetUpload.isCompleted()) {
                onEvent(SendRequestEvent.SEND_REQUEST_READY);
            }
            assignTargetUpload(targetUpload);
        }
    }

    public void requestSend() {
        initializeRequestedAt();
        schedule.checkExecutableSchedule(this.requestedAt);
        onEvent(SendRequestEvent.SEND_REQUEST_REQUESTED);
    }

    private void initializeRequestedAt() {
        this.requestedAt = LocalDateTime.now();
    }

    public SendRequestEvent onEvent(SendRequestEvent event) {
        SendRequestState toState = this.state.onEvent(event);
        changeStatus(toState);
        this.event = event;
        return event;
    }

    private void changeStatus(SendRequestState toState) {
        this.state = toState;
    }

    public void assignTargetUpload(TargetUploadReport targetUpload) {
        this.currentTargetUpload = Objects.requireNonNull(targetUpload, "target_upload_report");
        changeStateByTargetUploadReport(targetUpload.getState().getCurrentCode());
    }

    public void changeStateByTargetUploadReport(TargetUploadStatus status) {
        this.state.validate();
        switch (status) {
            case WAITING -> onEvent(SendRequestEvent.SEND_REQUEST_UPDATED);
            case COMPLETED -> onEvent(SendRequestEvent.SEND_REQUEST_READY);
        }
    }

    public String generateRequestUploadDir() {
        String customerId = customerRequestKey.getCustomerId();
        return FileUtil.generateFilePaths(customerId, String.valueOf(this.id), channelType.code().toLowerCase());
    }

    public String customerRequestId() {
        return Optional.ofNullable(customerRequestKey)
                .map(CustomerRequestKey::getCustomerRequestId)
                .orElse(null);
    }

    public String customerId() {
        return Optional.ofNullable(customerRequestKey)
                .map(CustomerRequestKey::getCustomerId)
                .orElse(null);
    }

    public void readyForSendRequest(TargetUploadReport targetUploadReport) {
        if(targetUploadReport.isCompleted()) {
            onEvent(SendRequestEvent.SEND_REQUEST_READY);
        }
    }

    private boolean isEqualToCurrentTargetUpload(TargetUploadReport targetUploadReport) {
        return this.currentTargetUpload == targetUploadReport;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @PostLoad
    public void postLoad() {
        this.isNew = false;
    }

    @PrePersist
    public void prePersist() {
        if(this.createdAt == null) {
            initializeCreateAt();
        }
    }
}