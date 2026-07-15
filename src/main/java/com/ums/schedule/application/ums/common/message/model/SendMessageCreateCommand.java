package com.ums.schedule.application.ums.common.message.model;

import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.domain.request.SendRequest;

public record SendMessageCreateCommand(
        SendRequest sendRequest,
        MessageType messageType,
        String advertisingPrefix
) {
    public static SendMessageCreateCommand of(SendRequest sendRequest, MessageType messageType, String advertisingPrefix) {
        return new SendMessageCreateCommand(
                sendRequest,
                messageType,
                advertisingPrefix
        );
    }
}
