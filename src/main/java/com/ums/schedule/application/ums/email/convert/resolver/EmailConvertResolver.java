package com.ums.schedule.application.ums.email.convert.resolver;

import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.exception.EmailConvertTypeNotSupportedException;
import com.ums.schedule.application.ums.email.exception.EmailPolicyViolationException;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.convert.strategy.EmailMessageConvertStrategy;
import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailCode;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

import com.ums.schedule.common.code.mapper.exception.EnumMapperNotFoundException;
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
                .orElseThrow(() -> EmailConvertTypeNotSupportedException.of(ConvertType.valueOf(convertType.code())));

        return EmailConvertPolicy.of(convertType, result);
    }

    private EnumMapperValue resolveConvertType(String convertType, SecurityMail securityMail) {
        try {
            return Optional.ofNullable(convertType)
                    .map(type -> mapperFactory.findEnumMapperValue(EmailCode.CONVERT_TYPE, type))
                    .orElseGet(() -> resolveConvertTypeBySecurityPolicy(securityMail));
        } catch (EnumMapperNotFoundException e) {
            throw EmailConvertTypeNotSupportedException.of(convertType);
        }
    }

    private EnumMapperValue resolveConvertTypeBySecurityPolicy(SecurityMail securityMail) {
        return Optional.ofNullable(securityMail)
                .map(policy -> EnumMapperValue.fromEnumMapperType(ConvertType.HTML))
                .orElse(EnumMapperValue.fromEnumMapperType(ConvertType.NONE));
    }
}
