package com.ums.schedule.orchestrator.application.requests.handler;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendRequestHandlerFactory {
    private final List<SendRendRequestedHandler> handlers;

    public SendRendRequestedHandler findHandler(EnumMapperValue channelType) {
        SendRendRequestedHandler handler = handlers.stream()
                .filter(v -> v.supports(channelType))
                .findFirst()
                .orElseThrow();
        return handler;
    }
}
