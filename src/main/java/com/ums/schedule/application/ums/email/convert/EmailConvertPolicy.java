package com.ums.schedule.application.ums.email.convert;

import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;


public record EmailConvertPolicy(
        EnumMapperValue convertType,
        String bodyKey,
        SecurityMailPolicy securityMailPolicy,
        ConvertedAttachment convertedAttachment
) {
    public static EmailConvertPolicy of(EnumMapperValue convertType, EmailConvertResult result) {
        return new EmailConvertPolicy(
                convertType,
                result.bodyKey(),
                result.securityMail(),
                result.convertedAttachment()
        );
    }
}
