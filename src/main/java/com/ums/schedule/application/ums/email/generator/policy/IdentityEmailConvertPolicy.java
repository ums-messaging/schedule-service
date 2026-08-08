package com.ums.schedule.application.ums.email.generator.policy;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplate;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplateContent;
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
    public EmailConvertPolicy convert(RenderedTemplate context, TargetMessageData targetData) {
        RenderedTemplateContent body = Optional.ofNullable(context.body()).orElseThrow(() -> EmailContentMissingException.of(EmailMessageSection.BODY));
        return EmailConvertPolicy.of(
                ConvertType.NONE,
                body.template(),
                context.attachments()
        );
    }
}
