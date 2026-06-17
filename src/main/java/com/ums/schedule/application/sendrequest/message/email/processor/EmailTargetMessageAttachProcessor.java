package com.ums.schedule.application.sendrequest.message.email.processor;

import com.ums.schedule.application.sendrequest.message.email.result.EmailTargetMessageResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import org.springframework.stereotype.Component;

@Component
public class EmailTargetMessageAttachProcessor implements EmailTargetMessageProcessor {
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return ConvertTypeEnum.valueOf(mapperValue.code()).equals(ConvertTypeEnum.NONE);
    }

    @Override
    public EmailTargetMessageResult process(EmailAttachment message, SendTarget target) {
        return EmailTargetMessageResult.of(message, target);
    }
}
