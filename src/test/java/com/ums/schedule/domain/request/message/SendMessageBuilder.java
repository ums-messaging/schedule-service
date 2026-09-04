package com.ums.schedule.domain.request.message;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.common.code.message.MessageType;

public class SendMessageBuilder {
    private MessageType messageType;
    private String templateKey;
    private String messagePrefix;
    private SendRequest sendRequest;

    public static SendMessageBuilder builder() {
        return new SendMessageBuilder();
    }

    private SendMessageBuilder() {
        this.messageType = MessageType.NONE;
        this.templateKey = "my_template";
        this.messagePrefix = "(광고)";
    }

    public SendMessageBuilder messageType(MessageType messageType) {
        this.messageType = messageType;
        return this;
    }

    public SendMessageBuilder messagePrefix(String messagePrefix) {
        this.messagePrefix = messagePrefix;
        return this;
    }

    public SendMessageBuilder sendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        return this;
    }

    public SendMessage build() {
        return new SendMessage(
                null,
                templateKey,
                messageType,
                messagePrefix
        );
    }
}