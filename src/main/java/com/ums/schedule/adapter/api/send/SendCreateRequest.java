package com.ums.schedule.adapter.api.send;

import com.ums.schedule.adapter.api.target.SendTargetUploadRequest;
import com.ums.schedule.application.target.dto.SendTargetDto;

import java.util.List;

public record SendCreateRequest(
        String customerSendRequestId,
        Integer retryCnt,
        Long scheduleId,
        String senderKey,
        String templateKey,
        String uploadType,
        List<SendTargetUploadRequest> targetList
) {

}
