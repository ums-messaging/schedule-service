package com.ums.schedule.email.domain.common;

import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.email.domain.requests.result.SendResultReport;

public record SendResult(
        EmailResultCode resultCode,
        String resultMessage
) {
    public static SendResult of( String message) {

        return new SendResult(
                EmailResultCode.FAIL, message
        );
    }

    public static SendResult of() {
        return new SendResult(EmailResultCode.SUCCESS, null);
    }
}
