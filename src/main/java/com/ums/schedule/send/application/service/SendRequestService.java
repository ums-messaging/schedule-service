package com.ums.schedule.send.application.service;

import com.ums.schedule.repository.SendRequestRepository;
import com.ums.schedule.send.application.assembler.SendTargetAssembler;
import com.ums.schedule.send.application.model.command.SendRequestCommand;
import com.ums.schedule.send.application.resolver.SendRequestResolver;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.CustomerRequestKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SendRequestService {
    private final SendRequestRepository sendRequestRepository;
    private final Map<String, SendRequestResolver> resolverMap;
    private final SendTargetAssembler assembler;


    public SendRequest createSendRequest(SendRequestCommand command, String customerId) {
        CustomerRequestKey key = CustomerRequestKey.of(customerId, command.customerSendRequestId());
        existsCustomerKey(customerId, key);
        return null;
    }

    private void existsCustomerKey(String customerId, CustomerRequestKey key) {
        boolean is = sendRequestRepository.existsByCustomerIdAAndCustomerRequestId(customerId, key.getCustomerRequestId());
        if(is) {

        }
    }
}
