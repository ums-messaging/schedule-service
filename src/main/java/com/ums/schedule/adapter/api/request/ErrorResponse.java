package com.ums.schedule.adapter.api.request;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import org.springframework.validation.FieldError;

public record ErrorResponse(
        String field,
        String reason
) {
    public static ErrorResponse of(String field, EnumMapperValue messageCode) {
        return new ErrorResponse(field, messageCode.description());
    }


}
