package com.ums.schedule.adapter.api.request.request;

import com.ums.schedule.adapter.api.target.request.FileTargetUploadRequest;
import com.ums.schedule.adapter.api.target.request.JsonTargetUploadRequest;
import com.ums.schedule.adapter.api.target.request.SendTargetCreateRequest;
import com.ums.schedule.application.sendrequest.command.SendRequestCreateCommand;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;

import java.util.List;
import java.util.Optional;

public record SendRequestCreateRequest(
        Long scheduleId,
        String customerRequestKey,
        String senderKey,
        String templateKey,
        String messageType,
        Integer retryCnt,
        JsonTargetUploadRequest jsonUploadRequest,
        FileTargetUploadRequest fileUploadRequest
) {

    public SendRequestCreateCommand toCommand(String customerId, ChannelTypeEnum channelType, boolean exists) {
        return SendRequestCreateCommand.fromDto(customerId, channelType, exists, this);
    }

    public EnumMapperValue getUploadType() {
        return Optional.ofNullable(fileUploadRequest)
                .map(upload -> EnumMapperValue.fromEnumMapperType(TargetUploadTypeEnum.FILE))
                .orElse(EnumMapperValue.fromEnumMapperType(TargetUploadTypeEnum.JSON));
    }

    public List<TargetMessageData> toSendTargetDtos() {
        return Optional.ofNullable(jsonUploadRequest)
                .map(req -> req.targetList().stream()
                        .map(SendTargetCreateRequest::toTargetData)
                        .toList()
                ).orElseGet(() -> List.of());
    }
}
