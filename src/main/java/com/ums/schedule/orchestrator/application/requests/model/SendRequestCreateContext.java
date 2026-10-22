package com.ums.schedule.orchestrator.application.requests.model;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.adaptor.in.rest.requests.request.SendRequestCreateRequest;
import com.ums.schedule.orchestrator.domain.common.ProvideUrl;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;

public record SendRequestCreateContext(
        SendRequestUpload upload,
        Long scheduleId,
        String messageId,
        String senderKey,
        EnumMapperValue channelType
) {

    public static SendRequestCreateContext of(
                                              SendRequestUpload upload,
                                              SendRequestCreateRequest request,
                                              EnumMapperValue channelType) {
        return new SendRequestCreateContext(
                upload,
                request.scheduleId(),
                request.messageId(),
                request.senderKey(),
                channelType
        );
    }
}
