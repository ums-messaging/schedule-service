package com.ums.schedule.application.message;

import com.ums.schedule.application.template.result.TemplateResult;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.message.SendMessage;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.template.code.TemplateEnumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendMessageFactory {
    private final EnumMapperFactory factory;
    private final String messagePrefix = "(광고)";

    public SendMessage createSendMessage(SendRequest sendRequest, TemplateResult template) {
        EnumMapperValue templateType = factory.findEnumMapperValue(TemplateEnumMapper.TEMPLATE_TYPE, template.templateType());
        return SendMessage.of(sendRequest, templateType, messagePrefix);
    }
}
