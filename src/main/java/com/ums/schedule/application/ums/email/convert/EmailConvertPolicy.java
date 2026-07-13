package com.ums.schedule.application.ums.email.convert;

import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;

import java.util.List;
import java.util.stream.Stream;


public record EmailConvertPolicy(
        EnumMapperValue convertType,
        String bodyKey,
        ConvertedAttachment convertedAttachment
) {
    public static EmailConvertPolicy of(EnumMapperValue convertType, EmailConvertResult result) {
        return new EmailConvertPolicy(
                convertType,
                result.bodyKey(),
                result.convertedAttachment()
        );
    }

    public List<ConvertedAttachment> combineAndGetAttachments(EmailTemplateResult template) {
        return Stream.concat(
                Stream.ofNullable(convertedAttachment),
                template.convertAndAttachments().stream()
        ).toList();
    }
}
