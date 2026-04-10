package com.ums.schedule.send.application.model.command;

import java.util.List;

public record SendRequestCommand(
        String customerSendRequestId,
        Integer retryCnt,
        Long scheduleId,
        String senderKey,
        String templateKey,
        String uploadType,
        List<SendTargetCreateCommand> targetList
) {
}
