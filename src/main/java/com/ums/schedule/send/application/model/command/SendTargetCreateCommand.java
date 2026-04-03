package com.ums.schedule.send.application.model.command;

import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.domain.target.SendTarget;
import com.ums.schedule.send.domain.target.TargetAddress;

import java.util.Map;

public record SendTargetCreateCommand(
        String targetKey,
        String targetName,
        String email,
        String phoneNumber,
        String birthday,
        Map<String, Object> messageVariable
) {

}
