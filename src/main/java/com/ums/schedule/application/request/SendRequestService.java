package com.ums.schedule.application.request;

import com.ums.schedule.adapter.api.send.SendCreateRequest;
import com.ums.schedule.application.request.dto.SendRequestCommand;
import com.ums.schedule.application.schedule.ScheduleService;
import com.ums.schedule.code.EnumMapper;
import com.ums.schedule.code.EnumMapperFactory;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.code.schedule.ScheduleTypeEnum;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.code.send.SendRequestEnumMapper;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendRequestedEvent;
import com.ums.schedule.domain.request.CustomerRequestKey;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import com.ums.schedule.domain.schedule.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SendRequestService {
    private final EnumMapperFactory factory;
    private final ApplicationEventPublisher publisher;
    private final SendRequestRepository sendRequestRepository;
    private final ScheduleService scheduleService;

    public SendRequest createSendRequest(String customerId, ChannelTypeEnum channelType, SendCreateRequest request) {
        Schedule schedule = scheduleService.findScheduleById(request.scheduleId());
        boolean exists = existsCustomerKey(customerId, request.customerSendRequestId());
        EnumMapperValue channelTypeValue = EnumMapperValue.fromEnumMapperType(channelType);
        EnumMapperValue uploadTypeValue = factory.findEnumMapperValue(SendRequestEnumMapper.TARGET_UPLOAD_TYPE, request.uploadType());

//        Map<EnumMapper, EnumMapperValue> mapperValue = Map.of(SendRequestEnumMapper.CHANNEL_TYPE, channelTypeValue, SendRequestEnumMapper.TARGET_UPLOAD_TYPE, uploadTypeValue);
//        SendRequestCommand command = SendRequestCommand.of(schedule, customerKey, mapperValue, request);
//        SendRequestEvent event = SendRequestEvent.of(command);
//        SendRequest sendRequest = SendRequest.of(command);

        return null;
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
