package com.ums.schedule.send.application.model.dto;

import com.ums.schedule.send.application.model.command.SendRequestCommand;

public record SendRequestDto(
        String templateKey,
        String senderKey,
        Integer retryCnt,
        Integer totalCount
) {
    public static SendRequestDto of(SendRequestCommand command) {
        return new SendRequestDto(
                command.templateKey(),
                command.senderKey(),
                command.retryCnt(),
                command.targetList().size()
        );
    }
}
