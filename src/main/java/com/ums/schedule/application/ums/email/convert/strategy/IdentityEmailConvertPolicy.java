package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.application.exception.email.ConvertTypeNotSupportedException;
import org.springframework.stereotype.Component;

@Component
public class IdentityEmailConvertPolicy implements EmailMessageConvertStrategy {
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return ConvertType.NONE.equals(ConvertType.valueOf(mapperValue.code()));
    }

    @Override
    public EmailConvertResult convert(EnumMapperValue convertType, EmailConvertResolveCommand command) {
        if(!convertType.code().equals(ConvertType.NONE.code())) {
            throw ConvertTypeNotSupportedException.of();
        }
        return EmailConvertResult.of(command.body().key());
    }
}
