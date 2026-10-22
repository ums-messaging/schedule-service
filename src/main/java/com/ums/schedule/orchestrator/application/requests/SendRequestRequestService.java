package com.ums.schedule.orchestrator.application.requests;

import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.adaptor.out.queue.kafka.SendRequestEventKafkaDispatcher;
import com.ums.schedule.orchestrator.application.requests.handler.SendRendRequestedHandler;
import com.ums.schedule.orchestrator.application.requests.handler.SendRequestHandlerCommand;
import com.ums.schedule.orchestrator.application.requests.handler.SendRequestHandlerFactory;
import com.ums.schedule.orchestrator.application.requests.result.SendRequestRequestContext;
import com.ums.schedule.orchestrator.domain.event.SendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendRequestRequestService {
    private final SendRequestFileService queryService;
    private final SendRequestHandlerFactory factory;

    @Transactional
    public SendRequest request(Long requestId) {
        SendRequestRequestContext context = queryService.findSendRequestById(requestId);
        SendRequest sendRequest = context.request();
        executeHandler(context, sendRequest.getChannelType());
        return sendRequest;
    }

    private void executeHandler(SendRequestRequestContext context, ChannelType channelType) {
        SendRendRequestedHandler handler = factory.findHandler(EnumMapperValue.fromEnumMapperType(channelType));
        handler.handle(context);
    }
}
