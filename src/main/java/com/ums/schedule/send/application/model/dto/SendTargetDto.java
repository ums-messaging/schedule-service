package com.ums.schedule.send.application.model.dto;

import com.ums.schedule.send.application.model.command.SendTargetCreateCommand;
import lombok.Getter;

import java.util.Map;


public record SendTargetDto(
        String targetKey,
        String targetName,
        Map<String, Object> messageVariable
) {

    public static SendTargetDto fromCommand(SendTargetCreateCommand command) {
        return new SendTargetDto(command.targetKey(), command.targetName(), command.messageVariable());
    }
}
