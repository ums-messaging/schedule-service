package com.ums.schedule.orchestrator.domain.event;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.request.SendRequestEvent;
import com.ums.schedule.common.code.api.SendRequestErrorCode;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.SendRequestEntity;
import com.ums.schedule.orchestrator.application.requests.command.SendRequestCreateCommand;
import com.ums.schedule.orchestrator.application.requests.model.SendRequestCreateContext;
import com.ums.schedule.orchestrator.domain.event.customer.CustomerRequestKey;
import com.ums.schedule.orchestrator.domain.event.state.SendRequestCreateState;
import com.ums.schedule.orchestrator.domain.event.state.SendRequestState;
import com.ums.schedule.orchestrator.domain.exception.SendRequestDomainException;
import com.ums.schedule.orchestrator.domain.schedule.Schedule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendRequest {
    private Long id;
    private Long scheduleId;
    private Long uploadId;

    private ChannelType channelType;

    private SendRequestState state;
    private CustomerRequestKey customerRequestKey;
    private Integer retryCount;

    private LocalDateTime createdAt;
    private LocalDateTime requestedAt;

    public static SendRequest of(CustomerRequestKey customerKey, SendRequestCreateCommand command, Integer retryCount) {
        SendRequest request = new SendRequest();
        request.generateId();
        request.assignScheduleId(command.scheduleId());
        request.assignChannelType(command.channelType());
        request.assignCustomerKey(customerKey);
        request.initializeRetryCount(retryCount);
        request.initializeStatusAndEvent();
        return request;
    }

    private void assignScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    private void generateId() {
        this.id = TsidCreator.getTsid().toLong();
    }

    public void assignChannelType(EnumMapperValue channelType) {
        Objects.requireNonNull(channelType, "channel_type");
        this.channelType = ChannelType.valueOf(channelType.code());
    }

    private void initializeStatusAndEvent() {
        changeStatus(new SendRequestCreateState());
    }

    private void initializeRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    private void assignCustomerKey(CustomerRequestKey customerRequestKey) {
        this.customerRequestKey = Optional.ofNullable(customerRequestKey)
                .orElseThrow(() -> SendRequestDomainException.of(SendRequestErrorCode.DUPLICATED_CUSTOMER_KEY));
    }

    public void requestSend(Schedule schedule) {
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
        return event;
    }

    private void changeStatus(SendRequestState toState) {
        this.state = toState;
    }

    public String customerRequestId() {
        return Optional.ofNullable(customerRequestKey)
                .map(CustomerRequestKey::getCustomerkey)
                .orElse(null);
    }

    public String customerId() {
        return Optional.ofNullable(customerRequestKey)
                .map(CustomerRequestKey::getCustomerId)
                .orElse(null);
    }

    public static SendRequest fromEntity(SendRequestEntity entity) {
        return new SendRequest(entity);
    }

    private SendRequest(SendRequestEntity entity) {
        this.id = entity.getId();
        this.scheduleId = entity.getSchedule().getId();
        this.uploadId = entity.getSendRequestUpload().getId();
        this.channelType = ChannelType.valueOf(entity.getChannelType());
        this.customerRequestKey = CustomerRequestKey.of(entity.getCustomerId(), entity.getCustomerKey());
        this.retryCount = entity.getRetryCount();
        this.createdAt = entity.getCreatedAt();
        this.requestedAt = entity.getRequestedAt();
        this.state = SendRequestStatus.valueOf(entity.getStatus()).createStatus();
    }

    public SendRequestEntity toEntity() {
        return SendRequestEntity.fromDomain(this);
    }
}
