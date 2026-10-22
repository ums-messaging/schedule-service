package com.ums.schedule.email.application.message.generator.policy;

import com.ums.schedule.email.application.message.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.email.application.message.generator.resolver.ConvertMessageCommand;
import com.ums.schedule.email.domain.template.EmailTemplate;
import org.springframework.stereotype.Component;


@Component
public class IdentityEmailConvertPolicy implements EmailMessageConvertPolicy {
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return ConvertType.NONE == ConvertType.valueOf(mapperValue.code());
    }


    @Override
    public EmailConvertPolicy convert(ConvertMessageCommand command) {
        EmailTemplate template = command.template();
        return EmailConvertPolicy.of(template.getBody());
    }
}
