package com.ums.schedule.send.application.model.command;

import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.domain.target.SendTarget;

import java.util.Map;

public record SendTargetCreateCommand(
        String targetKey,
        String targetAddress,
        String targetName,
        Map<String, Object> messageVariable
) {
    public SendTargetDto toDto() {
        return SendTargetDto.fromCommand(this);
    }
}
