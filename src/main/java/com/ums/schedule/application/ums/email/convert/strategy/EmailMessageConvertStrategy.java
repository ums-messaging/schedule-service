package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.common.code.mapper.EnumMapperSelector;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

public interface EmailMessageConvertStrategy extends EnumMapperSelector {
    EmailConvertResult convert(EnumMapperValue convertType, EmailConvertResolveCommand command);
}
