package com.ums.schedule.application.ums.email.convert.strategy.model;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;

public record EmailConvertResult(
        String bodyKey,
        ConvertedAttachment convertedAttachment
) {
    public static EmailConvertResult of(String bodyKey, ConvertedAttachment convertedAttachment) {
        return new EmailConvertResult(bodyKey, convertedAttachment);
    }

    public static EmailConvertResult of(String bodyKey) {
        return new EmailConvertResult(bodyKey, null);
    }

}
