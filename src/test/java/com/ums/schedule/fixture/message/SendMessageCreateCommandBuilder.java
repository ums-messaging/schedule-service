package com.ums.schedule.fixture.message;

import com.ums.schedule.application.ums.common.message.model.SendMessageCreateCommand;
import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.domain.sendrequest.SendRequest;

public class SendMessageCreateCommandBuilder {
    private SendRequest sendRequest;
    private MessageType messageType;
    private String messagePrefix;

    public static SendMessageCreateCommandBuilder builder() {
        return new SendMessageCreateCommandBuilder();
    }

    private SendMessageCreateCommandBuilder() {
        this.messageType = MessageType.NONE;
        this.messagePrefix = "(광고)";
    }

    public SendMessageCreateCommandBuilder sendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        return this;
    }

    public SendMessageCreateCommandBuilder messageType(MessageType messageType) {
        this.messageType = messageType;
        return this;
    }

    public SendMessageCreateCommandBuilder messagePrefix(String messagePrefix) {
        this.messagePrefix = messagePrefix;
        return this;
    }

    public SendMessageCreateCommand build() {
        return new SendMessageCreateCommand(
                sendRequest,
                messageType,
                messagePrefix
        );
    }
}
