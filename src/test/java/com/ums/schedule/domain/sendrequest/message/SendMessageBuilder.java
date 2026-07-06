package com.ums.schedule.domain.sendrequest.message;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.template.code.TemplateTypeEnum;

public class SendMessageBuilder {
    private TemplateTypeEnum templateType;
    private String messagePrefix;
    private SendRequest sendRequest;

    public static SendMessageBuilder builder() {
        return new SendMessageBuilder();
    }

    private SendMessageBuilder() {
        this.templateType = TemplateTypeEnum.NONE;
        this.messagePrefix = "(광고)";
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
