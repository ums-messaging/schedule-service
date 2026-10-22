package com.ums.schedule.email.application.message.generator.resolver;

import com.ums.schedule.email.application.message.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.email.application.exception.EmailConvertTypeNotSupportedException;
import com.ums.schedule.email.application.message.generator.policy.EmailMessageConvertPolicy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ConvertMessageResolver {
    private final List<EmailMessageConvertPolicy> policies;

    public EmailConvertPolicy resolve(ConvertMessageCommand command) {
        EmailConvertPolicy convertPolicy = policies
                .stream()
                .filter(p -> p.supports(command.convertType()))
                .findFirst()
                .map(p -> p.convert(command))
                .orElseThrow(() -> EmailConvertTypeNotSupportedException.of(command.convertType().code()));
        return convertPolicy;
    }
}
