package com.ums.schedule.fixture.sendrequest;

import com.ums.schedule.application.sendrequest.command.SendRequestCreateCommand;
import com.ums.schedule.application.sendrequest.command.SendRequestUpdateCommand;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.customer.CustomerRequestKey;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;
import com.ums.schedule.domain.request.state.SendRequestCreateState;
import com.ums.schedule.domain.request.state.SendRequestState;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.request.target.upload.TargetUploadReport;

import java.time.LocalDateTime;
import java.util.UUID;

public class SendRequestEntityBuilder {
    private Long id;
    private Integer retryCnt;
    private String senderKey;
    private String templateKey;
    private ChannelType channelType;
    private SendRequestState state;
    private CustomerRequestKey customerRequestKey;
    private TargetUploadReport currentTargetUpload;
    private Schedule schedule;
    private SendMessage sendMessage;

    private LocalDateTime createdAt;
    private LocalDateTime requestedAt;
    private LocalDateTime sendStartedAt;
    private LocalDateTime sendEndedAt;


    public static SendRequestEntityBuilder builder() {
        return new SendRequestEntityBuilder();
    }

    private SendRequestEntityBuilder() {
        CustomerRequestKey customerKey = CustomerRequestKey.of("jang314", "request123");

        this.senderKey = "test@test.com";
        this.templateKey = UUID.randomUUID().toString();
        this.channelType = ChannelType.EMAIL;
        this.state = new SendRequestCreateState();
        this.customerRequestKey = customerKey;
        this.retryCnt = 3;
    }

    public SendRequestEntityBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public SendRequestEntityBuilder templateKey(String templateKey) {
        this.templateKey = templateKey;
        return this;
    }

    public SendRequestEntityBuilder channelType(ChannelType channelType) {
        this.channelType = channelType;
        return this;
    }

    public SendRequestEntityBuilder retryCnt(Integer retryCnt) {
        this.retryCnt = retryCnt;
        return this;
    }

    public SendRequestEntityBuilder customerRequestKey(String customerKey, String requestKey) {
        this.customerRequestKey = CustomerRequestKey.of(customerKey, requestKey);
        return this;
    }

    public SendRequestEntityBuilder senderKey(String senderKey) {
        this.senderKey = senderKey;
        return this;
    }

    public SendRequestEntityBuilder state(SendRequestState state) {
        this.state = state;
        return this;
    }

    public SendRequestEntityBuilder currentTargetUpload(TargetUploadReport currentTargetUpload) {
        this.currentTargetUpload = currentTargetUpload;
        return this;
    }

    public SendRequestEntityBuilder schedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }
    public SendRequestCreateCommand toCreateCommand() {
        return toCreateCommand(TargetUploadTypeEnum.JSON, false);
    }

    public SendRequestCreateCommand toCreateCommand(TargetUploadTypeEnum uploadType, boolean exists) {
        return new SendRequestCreateCommand(
                schedule.getId(),
                channelType,
                uploadType,
                    "csv",
                    customerRequestKey.getCustomerId(),
                    customerRequestKey.getCustomerRequestId(),
                    templateKey,
                    senderKey,
                    retryCnt
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
                sendMessage,
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
