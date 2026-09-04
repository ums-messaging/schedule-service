package com.ums.schedule.application.ums.email.generator.policy;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.mapper.EnumMapperSelector;

public interface EmailMessageConvertPolicy extends EnumMapperSelector {
    EmailConvertPolicy convert(EmailTemplate template, TargetMessageData targetData);
}
