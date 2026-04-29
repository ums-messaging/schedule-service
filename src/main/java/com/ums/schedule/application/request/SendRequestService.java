package com.ums.schedule.application.request;

import com.ums.schedule.adapter.api.send.SendCreateRequest;
import com.ums.schedule.application.schedule.ScheduleService;
import com.ums.schedule.code.schedule.ScheduleTypeEnum;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendRequestedEvent;
import com.ums.schedule.domain.request.CustomerRequestKey;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import com.ums.schedule.domain.schedule.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendRequestService {
    private final ApplicationEventPublisher publisher;
    private final SendRequestRepository sendRequestRepository;
    private final ScheduleService scheduleService;

    public SendRequest createSendRequest(String customerId, ChannelTypeEnum channelType, SendCreateRequest command) {
        Schedule schedule = scheduleService.findScheduleById(command.scheduleId());
        boolean exists = existsCustomerKey(customerId, command.customerSendRequestId());
        CustomerRequestKey customerKey = CustomerRequestKey.of(customerId, command.customerSendRequestId(), exists);

        SendRequestEvent event = SendRequestEvent.of(schedule, customerKey, channelType);
        SendRequest request = event.getSendRequest();
        request.initRetryMaxCount(command.retryCnt());
        request.setSenderAndTemplateKey(command.senderKey(), command.templateKey());

        return request;
    }

    private boolean existsCustomerKey(String customerId, String customerRequestId) {
//        return sendRequestRepository.existsByCustomerIdAAndCustomerRequestId(customerId, customerRequestId);
        return false;
    }


    public void requestSendRequest(Long requestId) {
        SendRequest request = sendRequestRepository.findById(requestId).orElseThrow();
        SendRequestedEvent event = SendRequestedEvent.of(request);

        Schedule schedule = request.getSchedule();
        ScheduleTypeEnum scheduleType = schedule.getCyclePolicy().getScheduleType();
        if(scheduleType == ScheduleTypeEnum.REALTIME) {
            publisher.publishEvent(event);
        }
    }
}
