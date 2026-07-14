package com.ums.schedule.domain.sendrequest.message;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.common.code.message.MessageType;

public class SendMessageBuilder {
    private MessageType templateType;
    private String messagePrefix;
    private SendRequest sendRequest;

    public static SendMessageBuilder builder() {
        return new SendMessageBuilder();
    }

    private SendMessageBuilder() {
        this.templateType = MessageType.NONE;
        this.messagePrefix = "(광고)";
    }

    public SendMessageBuilder messageType(MessageType templateType) {
        this.templateType = templateType;
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
                templateType,
                messagePrefix,
                sendRequest
        );
    }
}
