package com.ums.schedule.application.request;

import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import com.ums.schedule.application.schedule.ScheduleService;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.application.target.upload.TargetUploadService;
import com.ums.schedule.adapter.api.send.SendCreateRequest;
import com.ums.schedule.domain.request.CustomerRequestKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class SendRequestService {
    private final ScheduleService scheduleService;
    private final SendRequestRepository sendRequestRepository;
    private final Map<String, TargetUploadService> targetUploadServiceMap;

    public SendRequest create(String customerId, SendCreateRequest command, SendRequest sendRequest) {
        CustomerRequestKey key = CustomerRequestKey.of(customerId, command.customerSendRequestId());
        existsCustomerKey(customerId, key);

        Schedule schedule = scheduleService.findScheduleById(command.scheduleId());

        sendRequest.applySchedule(schedule);
        sendRequest.applyCustomerRequestKey(key);
        sendRequest.initRetryMaxCount(command.retryCnt());
        sendRequest.setSenderAndTemplateKey(command.senderKey(), command.templateKey());

        return sendRequest;
    }


    private void existsCustomerKey(String customerId, CustomerRequestKey key) {
        boolean is = sendRequestRepository.existsByCustomerIdAAndCustomerRequestId(customerId, key.getCustomerRequestId());
        if(is) {

        }
    }
}
