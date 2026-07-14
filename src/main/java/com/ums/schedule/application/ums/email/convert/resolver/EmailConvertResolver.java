package com.ums.schedule.application.ums.email.convert.resolver;

import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.convert.strategy.EmailMessageConvertStrategy;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.application.exception.email.ConvertTypeNotSupportedException;
import com.ums.schedule.common.code.email.ConvertTypeEnum;
import com.ums.schedule.common.code.template.TemplateEnumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EmailConvertResolver {
    private final EnumMapperFactory mapperFactory;
    private final List<EmailMessageConvertStrategy> policyStrategies;

    public EmailConvertPolicy resolve(EmailConvertResolveCommand command, SecurityMail securityMail) {
        EnumMapperValue convertType = resolveConvertType(command.convertType(), securityMail);

        EmailConvertResult result = policyStrategies
                .stream()
                .filter(policy -> policy.supports(convertType))
                .map(policy -> policy.convert(convertType, command))
                .findFirst()
                .orElseThrow(ConvertTypeNotSupportedException::of);

        return EmailConvertPolicy.of(convertType, result);
    }

    private EnumMapperValue resolveConvertType(String convertType, SecurityMail securityMail) {
        return Optional.ofNullable(convertType)
                .map(type -> mapperFactory.findEnumMapperValue(TemplateEnumMapper.CONVERT_TYPE, type))
                .orElseGet(() -> resolveConvertTypeBySecurityPolicy(securityMail));
    }

    private EnumMapperValue resolveConvertTypeBySecurityPolicy(SecurityMail securityMail) {
        return Optional.ofNullable(securityMail)
                .map(policy -> EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.HTML))
                .orElse(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE));
    }
}
