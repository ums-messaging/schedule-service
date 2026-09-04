package com.ums.schedule.application.ums.common.message.model;

import com.ums.schedule.application.ums.common.template.model.TemplateResult;
import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.domain.request.SendRequest;

public record SendMessageCreateCommand(
        MessageType messageType,
        String advertisingPrefix,
        String templateKey
) {
    public static SendMessageCreateCommand of(TemplateResult template, MessageType messageType, String advertisingPrefix) {
        return new SendMessageCreateCommand(
                messageType,
                advertisingPrefix,
                template.templateId()
        );
    }
}
