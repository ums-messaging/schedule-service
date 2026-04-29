package com.ums.schedule.application.target.upload;

import com.ums.schedule.adapter.api.target.SendTargetUploadRequest;
import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.domain.request.SendRequest;

import java.util.List;

public record TargetUploadCreateCommand(
        Long requestId,
        ChannelTypeEnum channelType,
        String customerId,
        String templateKey,
        List<SendTargetDto> targetDtoList
) {
    public static TargetUploadCreateCommand of(String customerId, SendRequest sendRequest, List<SendTargetUploadRequest> requestList) {
        List<SendTargetDto> targetDtos = requestList.stream()
                .map(req -> SendTargetDto.of(req))
                .toList();
        return new TargetUploadCreateCommand(
                sendRequest.getId(),
                sendRequest.getChannelType(),
                customerId,
                sendRequest.getTemplateKey(),
                targetDtos
        );
    }
}
