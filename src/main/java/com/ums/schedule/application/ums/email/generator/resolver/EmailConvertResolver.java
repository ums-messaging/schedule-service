package com.ums.schedule.application.ums.email.generator.resolver;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.email.exception.EmailConvertTypeNotSupportedException;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.generator.policy.EmailMessageConvertPolicy;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EmailConvertResolver {
    private final List<EmailMessageConvertPolicy> policies;

    public EmailConvertPolicy resolve(EmailTemplate template, TargetMessageData targetData) {
        EnumMapperValue convertType = EnumMapperValue.fromEnumMapperType(template.getConvertType());

        EmailConvertPolicy convertPolicy = policies
                .stream()
                .filter(policy -> policy.supports(convertType))
                .findFirst()
                .map(policy -> policy.convert(template, targetData))
                .orElseThrow(() -> EmailConvertTypeNotSupportedException.of(template.getConvertType()));

        return convertPolicy;
    }
}
