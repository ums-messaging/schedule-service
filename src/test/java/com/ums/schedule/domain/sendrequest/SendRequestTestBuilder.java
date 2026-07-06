package com.ums.schedule.domain.sendrequest;

import com.ums.schedule.application.sendrequest.command.SendRequestCreateCommand;
import com.ums.schedule.application.sendrequest.command.SendRequestUpdateCommand;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.customer.CustomerRequestKey;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;
import com.ums.schedule.domain.sendrequest.state.SendRequestCreateState;
import com.ums.schedule.domain.sendrequest.state.SendRequestState;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;

import java.time.LocalDateTime;
import java.util.UUID;

public class SendRequestTestBuilder {
    private Long id;
    private Integer retryCnt;
    private String senderKey;
    private String templateKey;
    private ChannelTypeEnum channelType;
    private SendRequestState state;
    private CustomerRequestKey customerRequestKey;
    private TargetUploadReport currentTargetUpload;
    private Schedule schedule;

    private LocalDateTime createdAt;
    private LocalDateTime requestedAt;
    private LocalDateTime sendStartedAt;
    private LocalDateTime sendEndedAt;


    public static SendRequestTestBuilder builder() {
        return new SendRequestTestBuilder();
    }

    private SendRequestTestBuilder() {
        CustomerRequestKey customerKey = CustomerRequestKey.of("jang314", "request123");

        this.senderKey = "test@test.com";
        this.templateKey = UUID.randomUUID().toString();
        this.channelType = ChannelTypeEnum.EMAIL;
        this.state = new SendRequestCreateState();
        this.customerRequestKey = customerKey;
        this.retryCnt = 3;
    }

    public SendRequestTestBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public SendRequestTestBuilder templateKey(String templateKey) {
        this.templateKey = templateKey;
        return this;
    }

    public SendRequestTestBuilder channelType(ChannelTypeEnum channelType) {
        this.channelType = channelType;
        return this;
    }

    public SendRequestTestBuilder retryCnt(Integer retryCnt) {
        this.retryCnt = retryCnt;
        return this;
    }

    public SendRequestTestBuilder customerRequestKey(String customerKey, String requestKey) {
        this.customerRequestKey = CustomerRequestKey.of(customerKey, requestKey);
        return this;
    }

    public SendRequestTestBuilder senderKey(String senderKey) {
        this.senderKey = senderKey;
        return this;
    }

    public SendRequestTestBuilder state(SendRequestState state) {
        this.state = state;
        return this;
    }

    public SendRequestTestBuilder currentTargetUpload(TargetUploadReport currentTargetUpload) {
        this.currentTargetUpload = currentTargetUpload;
        return this;
    }

    public SendRequestTestBuilder schedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }
    public SendRequestCreateCommand toCreateCommand() {
        return toCreateCommand(TargetUploadTypeEnum.JSON, false);
    }

    public SendRequestCreateCommand toCreateCommand(TargetUploadTypeEnum uploadType, boolean exists) {
        return new SendRequestCreateCommand(
                schedule.getId(),
                EnumMapperValue.fromEnumMapperType(channelType),
                    EnumMapperValue.fromEnumMapperType(uploadType),
                    "csv",
                    customerRequestKey.getCustomerId(),
                    customerRequestKey.getCustomerRequestId(),
                    templateKey,
                    senderKey,
                    retryCnt,
                    exists
                );
    }

    public SendRequest build() {
        SendRequest sendRequest = new SendRequest(
                id,
                retryCnt,
                templateKey,
                senderKey,
                channelType,
                state,
                null,
                customerRequestKey,
                currentTargetUpload,
                schedule,
                createdAt,
                requestedAt,
                sendStartedAt,
                sendEndedAt
        );

        return sendRequest;
    }

    public SendRequestUpdateCommand toUpdateCommand() {
        return new SendRequestUpdateCommand(
                this.templateKey,
                this.senderKey,
                this.retryCnt
        );
    }
}
