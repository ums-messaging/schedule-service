package com.ums.schedule.application.sendrequest.message.email.processor;

import com.ums.schedule.application.sendrequest.message.email.result.EmailTargetMessageResult;
import com.ums.schedule.common.code.mapper.EnumMapperSelector;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.target.SendTarget;

public interface EmailTargetMessageProcessor extends EnumMapperSelector {
    EmailTargetMessageResult process(EmailAttachment message, SendTarget target) ;
}
