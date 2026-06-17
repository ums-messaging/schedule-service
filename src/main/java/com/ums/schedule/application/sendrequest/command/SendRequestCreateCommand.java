package com.ums.schedule.application.sendrequest.command;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;

import java.util.Optional;

public record SendRequestCreateCommand(
        Long scheduleId,
        EnumMapperValue channel,
        EnumMapperValue uploadType,
        String uploadFormat,
        String customerId,
        String customerKey,
        String templateKey,
        String senderKey,
        Integer retryCnt,
        boolean exists
) {

    public static SendRequestCreateCommand fromDto(String customerId, ChannelTypeEnum channelType, boolean exists, SendRequestCreateRequest request) {
        EnumMapperValue channelTypeValue = Optional.ofNullable(channelType)
                .map(ch -> EnumMapperValue.fromEnumMapperType(ch)).orElse(null);

        return new SendRequestCreateCommand(
                request.scheduleId(),
                channelTypeValue,
                Optional.ofNullable(request.fileUploadRequest())
                        .map(req -> EnumMapperValue.fromEnumMapperType(TargetUploadTypeEnum.FILE))
                        .orElseGet(() -> EnumMapperValue.fromEnumMapperType(TargetUploadTypeEnum.JSON)),
                Optional.ofNullable(request.fileUploadRequest())
                        .map(req -> req.uploadFormat())
                        .orElse(null),
                customerId,
                request.customerRequestKey(),
                request.templateKey(),
                request.senderKey(),
                request.retryCnt(),
                exists
        );
    }
}
