package com.ums.schedule.orchestrator.application.upload.command;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.adaptor.in.rest.upload.request.SendRequestUploadCreateRequest;

public record SendRequestUploadCreateCommand(
        EnumMapperValue type,
        String customerId,
        String title,
        String description
) {
    public static SendRequestUploadCreateCommand of(EnumMapperValue type, String customerId,
                                                    SendRequestUploadCreateRequest request) {
        return new SendRequestUploadCreateCommand(
                type,
                customerId,
                request.title(),
                request.description()
        );
    }
}
