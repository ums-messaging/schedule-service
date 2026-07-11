package com.ums.schedule.application.ums.email.convert.strategy.model;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

import java.util.List;

public record EmailConvertResult(
        String bodyKey,
        List<ConvertedAttachment> convertedAttachments
) {
    public static EmailConvertResult of(String bodyKey, List<ConvertedAttachment> contexts) {
        return new EmailConvertResult(bodyKey, contexts);
    }

}
