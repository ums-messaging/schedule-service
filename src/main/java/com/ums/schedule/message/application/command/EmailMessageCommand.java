package com.ums.schedule.message.application.command;

import com.ums.schedule.attachment.application.command.SecurityPolicyCommand;
import com.ums.schedule.send.application.model.command.SendRequestCommand;
import com.ums.schedule.send.application.model.dto.SendTargetDto;

import java.util.List;

public record EmailMessageCommand(
        SendRequestCommand sendRequest,
        String convertType,
        String encodingType,
        String attachmentNameFormat,
        String downloadNameFormat,
        SecurityPolicyCommand securityPolicy
) {
    public List<SendTargetDto> toDtos(String uploadId) {
        return sendRequest.targetList()
                .stream()
                .map(command -> SendTargetDto.of(command, uploadId))
                .toList();
    }
}
