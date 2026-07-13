package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertPolicyContext;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.exception.ConvertTypeNotSupportedException;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class IdentityEmailConvertPolicy implements EmailMessageConvertStrategy {
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return ConvertTypeEnum.NONE.equals(ConvertTypeEnum.valueOf(mapperValue.code()));
    }

    @Override
    public EmailConvertResult convert(EnumMapperValue convertType, EmailConvertResolveCommand command) {
        if(!convertType.code().equals(ConvertTypeEnum.NONE.code())) {
            throw ConvertTypeNotSupportedException.of();
        }
        return EmailConvertResult.of(command.body().key());
    }
}
