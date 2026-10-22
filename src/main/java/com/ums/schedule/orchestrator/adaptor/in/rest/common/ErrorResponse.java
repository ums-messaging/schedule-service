package com.ums.schedule.orchestrator.adaptor.in.rest.common;

import com.ums.schedule.common.code.mapper.EnumMapperValue;

public record ErrorResponse(
        String field,
        String reason
) {
    public static ErrorResponse of(String field, EnumMapperValue messageCode) {
        return new ErrorResponse(field, messageCode.description());
    }


}
