package com.ums.schedule.application.ums.common.message;

import com.ums.schedule.application.ums.common.config.SendMessageProperties;
import com.ums.schedule.application.ums.common.exception.SendMessageNotConfiguredException;
import com.ums.schedule.application.ums.common.message.model.SendMessageCreateCommand;
import com.ums.schedule.application.ums.common.template.model.TemplateResult;
import com.ums.schedule.common.code.api.SendMessageErrorCode;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.message.MessageConfigurationPrefix;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.common.code.message.MessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SendMessageFactory {
    private final EnumMapperFactory factory;
    private final SendMessageProperties properties;

    public SendMessage createSendMessage(TemplateResult template) {
        MessageType messageType = getMessageType(template.templateType());
        String advertisePrefix = getAdvertisingPrefix(messageType);

        SendMessageCreateCommand command =
                SendMessageCreateCommand.of(messageType, advertisePrefix);

        return SendMessage.of(command);
    }

    private MessageType getMessageType(String templateType) {
        EnumMapperValue messageTypeValue = factory.findEnumMapperValue(MessageCode.MESSAGE_TYPE, templateType);

        return Optional.ofNullable(messageTypeValue)
                .map(v -> MessageType.valueOf(v.code()))
                .orElseGet(() -> MessageType.NONE);
    }

    private String getAdvertisingPrefix(MessageType messageType) {
        if(messageType == MessageType.ADVERTISE) {
            return Optional.ofNullable(properties.getAdvertisingPrefix())
                    .filter(StringUtils::hasText)
                    .orElseThrow(() -> SendMessageNotConfiguredException.of(MessageConfigurationPrefix.ADVERTISE_PREFIX));
        }
        return null;
    }
}
