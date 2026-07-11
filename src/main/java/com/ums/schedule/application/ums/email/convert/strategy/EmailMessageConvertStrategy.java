package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertPolicyContext;
import com.ums.schedule.common.code.mapper.EnumMapperSelector;


public interface EmailMessageConvertStrategy extends EnumMapperSelector {
    EmailConvertResult convert(EmailConvertPolicyContext command);
}
