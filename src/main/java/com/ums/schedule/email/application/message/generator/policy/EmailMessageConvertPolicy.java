package com.ums.schedule.email.application.message.generator.policy;

import com.ums.schedule.common.code.mapper.EnumMapperSelector;
import com.ums.schedule.email.application.message.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.email.application.message.generator.resolver.ConvertMessageCommand;

public interface EmailMessageConvertPolicy extends EnumMapperSelector {
    EmailConvertPolicy convert(ConvertMessageCommand command);
}
