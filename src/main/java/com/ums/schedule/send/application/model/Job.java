package com.ums.schedule.send.application.model;

import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;

import java.util.UUID;

// status도 기록
public record Job (
        String jobId,
        String requestId,
        String messageId,
        ConvertTypeEnum convertType,
        String convertKey

) {
}
