package com.ums.schedule.email.domain.event;

import com.ums.schedule.common.code.email.EmailResultCode;

public record SendGroupEventResult(
        EmailResultCode resultCode,
        String resultMessage
) {
    public static SendGroupEventResult of() {
        return new SendGroupEventResult(
                EmailResultCode.SUCCESS,
                null
        );
    }
    public static SendGroupEventResult of(EmailResultCode resultCode, String resultMessage) {
        return new SendGroupEventResult(resultCode, resultMessage);
    }
}
