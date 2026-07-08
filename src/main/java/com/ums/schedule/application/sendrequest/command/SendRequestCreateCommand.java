package com.ums.schedule.application.sendrequest.command;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;
import com.ums.schedule.application.sendrequest.context.SendRequestCreateContext;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.customer.CustomerRequestKey;
import com.ums.schedule.domain.sendrequest.message.SendMessage;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;

public record SendRequestCreateCommand(
        Long scheduleId,
        ChannelTypeEnum channelType,
        TargetUploadTypeEnum uploadType,
        String uploadFormat,
        String customerId,
        String customerKey,
        String templateKey,
        String senderKey,
        Integer retryCnt
) {

    public static SendRequestCreateCommand of(String customerId, ChannelTypeEnum channelType, TargetUploadTypeEnum uploadType, SendRequestCreateRequest request) {
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
