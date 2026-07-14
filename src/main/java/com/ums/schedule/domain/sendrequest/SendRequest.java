package com.ums.schedule.domain.sendrequest;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.application.sendrequest.command.SendRequestUpdateCommand;
import com.ums.schedule.application.sendrequest.context.SendRequestCreateContext;
import com.ums.schedule.common.exception.validation.DuplicateViolationException;
import com.ums.schedule.common.util.FileUtil;
import com.ums.schedule.common.util.ValidationUtils;
import com.ums.schedule.common.code.request.SendRequestEventEnum;
import com.ums.schedule.common.code.common.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.converter.ChannelTypeConverter;
import com.ums.schedule.domain.sendrequest.customer.CustomerRequestKey;
import com.ums.schedule.domain.exception.request.DefaultRetryCountNotConfiguredException;
import com.ums.schedule.domain.exception.request.SendMessageNotFoundException;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.sendrequest.state.SendRequestCreateState;
import com.ums.schedule.domain.sendrequest.state.SendRequestState;
import com.ums.schedule.domain.sendrequest.converter.SendRequestStateConverter;
import com.ums.schedule.domain.exception.schedule.ScheduleNotFoundException;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.exception.target_upload.InvalidTargetUploadReportMismatchException;
import com.ums.schedule.domain.exception.target_upload.TargetUploadReportNotFoundException;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "send_request", uniqueConstraints = {
            @UniqueConstraint(
                    name="uq_customer_request",
                    columnNames = {"customer_id", "customer_request_id"}
            )})
@Getter
@AllArgsConstructor
public class SendRequest {
    @Id
    @Tsid
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
    private ChannelTypeEnum channelType;

    @Convert(converter = SendRequestStateConverter.class)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(10) default 'CREATE'")
    private SendRequestState state;

    @Transient
    private SendRequestEventEnum event;

    @Embedded
    private CustomerRequestKey customerRequestKey;

    @JoinColumn(name = "upload_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private TargetUploadReport currentTargetUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @OneToOne
    @JoinColumn(name = "message_id", nullable = false)
    private SendMessage sendMessage;

    private LocalDateTime createdAt;
    private LocalDateTime requestedAt;
    private LocalDateTime sendStartedAt;
    private LocalDateTime sendCompletedAt;

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
        this.sendMessage = Optional.ofNullable(sendMessage)
                .orElseThrow(SendMessageNotFoundException::of);
    }

    private void assignChannelType(ChannelTypeEnum channelType) {
        ValidationUtils.isEmpty("channel_type", channelType);
        this.channelType = channelType;
    }

    private void assignCurrentTargetUploadReport(TargetUploadReport targetUploadReport) {
        if(targetUploadReport == null) {
            TargetUploadReportNotFoundException.of();
        }
        this.currentTargetUpload = targetUploadReport;
    }

    protected SendRequest() {
        this.id = TsidCreator.getTsid().toLong();
    }

    private void initializeStatusAndEvent() {
        this.event = SendRequestEventEnum.SEND_REQUEST_CREATED;
        changeStatus(new SendRequestCreateState());
    }
    private void assignSenderAndTemplate(String senderKey, String templateKey) {
        assignSender(senderKey);
        assignTemplate(templateKey);
    }
    private void assignSender(String senderKey) {
        ValidationUtils.isEmpty("sender_key", senderKey);
        this.senderKey = senderKey;
    }

    private void assignTemplate(String templateKey) {
        ValidationUtils.isEmpty("template_key", templateKey);
        this.templateKey = templateKey;
    }

    private void initializeRetryCount(Integer retryCount) {
        this.retryCnt = Optional.ofNullable(retryCount)
                .filter(count -> count >= 0)
                .orElseThrow(DefaultRetryCountNotConfiguredException::of);
    }

    private void initializeCreateAt() {
        this.createdAt = LocalDateTime.now();
    }

    private void assignSchedule(Schedule schedule) {
        checkScheduleExists(schedule);
        schedule.checkScheduleAvailability();
        this.schedule = schedule;
    }

    private void checkScheduleExists(Schedule schedule){
        if(schedule == null) {
            throw ScheduleNotFoundException.of();
        }
    }

    private void assignCustomerKey(CustomerRequestKey customerRequestKey) {
        this.customerRequestKey = Optional.ofNullable(customerRequestKey)
                .orElseThrow(() -> DuplicateViolationException.fieldOf("customer_key"));
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
        onEvent(SendRequestEventEnum.SEND_REQUEST_UPDATED);
        if(targetUpload != null) {
            if(targetUpload.isCompleted()) {
                onEvent(SendRequestEventEnum.SEND_REQUEST_READY);
            }
            assignTargetUpload(targetUpload);
        }
    }

    public void requestSend(Long totalTargetCount) {
        initializeRequestedAt();
        schedule.checkExecutableSchedule(this.requestedAt);
        onEvent(SendRequestEventEnum.SEND_REQUEST_REQUESTED);
    }

    private void initializeRequestedAt() {
        this.requestedAt = LocalDateTime.now();
    }

    public SendRequestEventEnum onEvent(SendRequestEventEnum event) {
        SendRequestState toState = this.state.onEvent(event);
        changeStatus(toState);
        this.event = event;
        return event;
    }

    private void changeStatus(SendRequestState toState) {
        this.state = toState;
    }

    public void assignTargetUpload(TargetUploadReport targetUpload) {
        this.currentTargetUpload = Optional.ofNullable(targetUpload)
                .orElseThrow(TargetUploadReportNotFoundException::of);
        changeStateByTargetUploadReport();
    }

    private void changeStateByTargetUploadReport() {
        this.state.validate();
        switch (currentTargetUpload.getState().getCurrentCode()) {
            case WAITING -> onEvent(SendRequestEventEnum.SEND_REQUEST_UPDATED);
            case COMPLETED -> onEvent(SendRequestEventEnum.SEND_REQUEST_READY);
        }
    }

    public String generateRequestUploadDir() {
        String customerId = customerRequestKey.getCustomerId();
        return FileUtil.generateFilePaths(customerId, String.valueOf(this.id), channelType.code().toLowerCase());
    }

    public void prepareForUpload(TargetUploadReport targetUploadReport) {
        if(this.currentTargetUpload != null) {
            validateCurrentTargetUploadReport(targetUploadReport);
            if(isValidTargetUploadState(targetUploadReport)) {
                onEvent(SendRequestEventEnum.SEND_REQUEST_UPDATED);
                return;
            }
        }
        throw TargetUploadReportNotFoundException.of();
    }

    public void readyForSendRequest(TargetUploadReport targetUploadReport) {
        validateCurrentTargetUploadReport(targetUploadReport);
        if(targetUploadReport.isCompleted()) {
            onEvent(SendRequestEventEnum.SEND_REQUEST_READY);
        }
    }

    private boolean isValidTargetUploadState(TargetUploadReport targetUploadReport) {
        return targetUploadReport.isReadyForUpload();
    }

    private void validateCurrentTargetUploadReport(TargetUploadReport targetUploadReport) {
        if(!isEqualToCurrentTargetUpload(targetUploadReport)) {
            throw InvalidTargetUploadReportMismatchException.of(this.currentTargetUpload.getId(), targetUploadReport.getId());
        }
    }

    private boolean isEqualToCurrentTargetUpload(TargetUploadReport targetUploadReport) {
        return this.currentTargetUpload == targetUploadReport;
    }

    public UUID getCurrentUploadId() {
        return this.currentTargetUpload.getId();
    }
}