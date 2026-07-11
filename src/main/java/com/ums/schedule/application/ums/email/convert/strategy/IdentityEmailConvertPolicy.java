package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertPolicyContext;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class IdentityEmailConvertPolicy implements EmailMessageConvertStrategy {
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return ConvertTypeEnum.NONE.equals(ConvertTypeEnum.valueOf(mapperValue.code()));
    }

    @Override
    public EmailConvertResult convert(EmailConvertPolicyContext command) {
        return EmailConvertResult.of(command.bodyKey(), command.attachments());
    }
}
