package com.ums.schedule.application.sendrequest.command;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;
import com.ums.schedule.application.ums.common.request.model.SendRequestCreateContext;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.request.customer.CustomerRequestKey;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;

public record SendRequestCreateCommand(
        Long scheduleId,
        ChannelType channelType,
        TargetUploadTypeEnum uploadType,
        String uploadFormat,
        String customerId,
        String customerKey,
        String templateKey,
        String senderKey,
        Integer retryCnt
) {

    public static SendRequestCreateCommand of(String customerId, ChannelType channelType, TargetUploadTypeEnum uploadType, SendRequestCreateRequest request) {
        return new SendRequestCreateCommand(
                request.scheduleId(),
                channelType,
                uploadType,
                request.uploadFormat(),
                customerId,
                request.customerRequestKey(),
                request.templateKey(),
                request.senderKey(),
                request.retryCnt()
        );
    }

    public SendRequestCreateContext toContext(Schedule schedule,
                                              CustomerRequestKey customerRequestKey,
                                              SendMessage message,
                                              Integer retryCount) {
        return SendRequestCreateContext.of(schedule,
                customerRequestKey,
                message,
                retryCount,
                this
        );
    }
}
