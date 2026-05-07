package com.ums.schedule.domain.request;

import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.request.state.SendRequestCreateState;
import com.ums.schedule.domain.request.state.SendRequestState;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadTestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendRequestTestBuilder {
    private String templateKey = UUID.randomUUID().toString();
    private Integer retryCnt = 3;
    private String senderKey = UUID.randomUUID().toString();
    private ChannelTypeEnum channelType = ChannelTypeEnum.EMAIL;
    private SendRequestState state = new SendRequestCreateState();
    private SendRequestStatusEnum status = SendRequestStatusEnum.CREATE;
    private CustomerRequestKey customerRequestKey
            = CustomerRequestKey.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(), false);
    private TargetUpload currentTargetUpload;
    private Schedule schedule;
    private List<TargetUpload> targetUploadList = new ArrayList<>();

    public static SendRequestTestBuilder builder() {
        return new SendRequestTestBuilder();
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
        this.customerRequestKey = new CustomerRequestKey(customerKey, requestKey);
        return this;
    }

    public SendRequestTestBuilder senderKey(String senderKey) {
        this.senderKey = senderKey;
        return this;
    }

    public SendRequestTestBuilder state(SendRequestState state) {
        this.state = state;
        this.status = (state == null) ? null : state.currentSendRequestStatus();
        return this;
    }

    public SendRequestTestBuilder currentTargetUpload(TargetUpload currentTargetUpload) {
        this.currentTargetUpload = currentTargetUpload;
        return this;
    }

    public SendRequestTestBuilder schedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }

    public SendRequestTestBuilder addTargetUpload(TargetUpload targetUpload) {
        this.targetUploadList.add(targetUpload);
        return this;
    }

    public SendRequest build() {
        SendRequest sendRequest = new SendRequest(
                null,
                templateKey,
                retryCnt,
                senderKey,
                channelType,
                state,
                status,
                customerRequestKey,
                currentTargetUpload,
                schedule,
                targetUploadList
        );

        return sendRequest;
    }
}
