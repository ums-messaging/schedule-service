package com.ums.schedule.application.sendrequest;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;
import com.ums.schedule.application.exception.ScheduleNotFoundException;
import com.ums.schedule.application.sendrequest.command.SendRequestCreateCommand;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.SendRequestRepository;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.customer.CustomerRequestKey;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendRequestService {
    private final ScheduleJpaRepository scheduleRepository;
    private final SendRequestRepository sendRequestRepository;

    @Transactional
    public SendRequest create(String customerId, ChannelTypeEnum channelType, SendRequestCreateRequest request) {
        Schedule schedule = scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> ScheduleNotFoundException.of(request.scheduleId()));

        CustomerRequestKey customerKey = CustomerRequestKey.of(customerId, request.customerRequestKey());
        boolean existsKey = sendRequestRepository.existsByCustomerRequestKey(customerKey);

        SendRequestCreateCommand command = request.toCommand(customerId, channelType, existsKey);
        SendRequest sendRequest = SendRequest.of(schedule, EnumMapperValue.fromEnumMapperType(channelType), customerKey, command);

        return sendRequestRepository.save(sendRequest);
    }
}
