package com.ums.schedule.send.application.model.command;

import com.ums.schedule.send.application.model.dto.SendRequestDto;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
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
    public SendRequestDto toDto() {
        return SendRequestDto.of(this);
    }

    public List<SendTargetDto> toTargetDtoList() {
        return targetList
                .stream()
                .map(target -> target.toDto())
                .toList();
    }
}
