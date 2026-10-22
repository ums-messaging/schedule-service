package com.ums.schedule.orchestrator.application.requests;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.adaptor.out.api.message.ChannelMessageClient;
import com.ums.schedule.orchestrator.adaptor.out.api.message.request.ChannelSendRequest;
import com.ums.schedule.orchestrator.application.channel.ChannelClientFactory;
import com.ums.schedule.orchestrator.application.requests.command.SendRequestCreateCommand;
import com.ums.schedule.orchestrator.application.requests.model.SendRequestCreateResult;
import com.ums.schedule.orchestrator.application.requests.result.SendRequestSaveResult;
import com.ums.schedule.orchestrator.domain.event.SendRequest;
import com.ums.schedule.orchestrator.domain.event.SendRequestRepository;
import com.ums.schedule.orchestrator.domain.event.customer.CustomerRequestKey;
import com.ums.schedule.orchestrator.domain.schedule.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendRequestCreateService {
    private final SendRequestRequestService requestService;
    private final SendRequestRepository sendRequestRepository;
    private final ChannelClientFactory clientFactory;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SendRequestCreateResult create(SendRequestCreateCommand command) {
        SendRequest sendRequest = createSendRequest(command);
        SendRequestSaveResult saved = sendRequestRepository.save(sendRequest, command.objectKey());

        Schedule schedule = saved.schedule();
        schedule.availableSchedulePeriodAndStatus();

        executeChannelClient(saved.sendRequest().getId(), command.channelType(), command.messageId(), command.senderKey());

        if(schedule.isRealtime()) {
            requestService.request(saved.sendRequest().getId());
        }

        return SendRequestCreateResult.of(command.messageId(), schedule, saved.sendRequest());
    }

    private SendRequest createSendRequest(SendRequestCreateCommand command) {
        CustomerRequestKey customerRequestKey = createCustomerRequestKey(command.customerId(), command.requestKey());
        Integer retryCount = getOrDefaultRetryCount(command.requestRetryCount(), command.configuredRetryCount());
        SendRequest sendRequest = SendRequest.of(customerRequestKey, command, retryCount);
        return sendRequest;
    }

    private Long executeChannelClient(Long id, EnumMapperValue channelType, String messageId, String senderKey) {
        ChannelMessageClient client = clientFactory.findChannelClient(channelType);
        ChannelSendRequest request = ChannelSendRequest.of(id, messageId, senderKey);
        return client.createChannelRequest(request);
    }

    private Integer getOrDefaultRetryCount(int requestRetryCount, int configuredRetryCount) {
        return requestRetryCount == 0 ? configuredRetryCount : requestRetryCount;
    }

    private CustomerRequestKey createCustomerRequestKey(String customerId, String customerRequestKey) {
        CustomerRequestKey key = CustomerRequestKey.of(customerId, customerRequestKey);
        if(sendRequestRepository.existsByCustomerIdAndCustomerKey(customerId, customerRequestKey)) {
            return null;
        }
        return key;
    }
}
