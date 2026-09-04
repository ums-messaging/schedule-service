package com.ums.schedule.application.ums.email.generator.policy;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplateContent;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.exception.EmailContentMissingException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IdentityEmailConvertPolicy implements EmailMessageConvertPolicy {
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return ConvertType.NONE == ConvertType.valueOf(mapperValue.code());
    }

    @Override
    public EmailConvertPolicy convert(EmailTemplate template, TargetMessageData targetData) {
        EmailTemplateContent bodyTemplate = Optional.ofNullable(template.getBody()).orElseThrow(() -> EmailContentMissingException.of(EmailMessageSection.BODY));
        return EmailConvertPolicy.of(
                ConvertType.NONE,
                bodyTemplate.template(),
                template.toPayloads()
        );
    }
}
