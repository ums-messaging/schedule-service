package com.ums.schedule.send.application.service;

import com.ums.schedule.message.application.assembler.EmailMessageAssembler;
import com.ums.schedule.repository.SendRequestRepository;
import com.ums.schedule.schedule.application.service.ScheduleService;
import com.ums.schedule.schedule.domain.Schedule;
import com.ums.schedule.send.application.assembler.TargetUploadService;
import com.ums.schedule.send.application.model.command.SendRequestCommand;
import com.ums.schedule.send.application.model.command.SendTargetCreateCommand;
import com.ums.schedule.send.application.model.dto.SendRequestDto;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.application.model.dto.TargetUploadDto;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.CustomerRequestKey;
import com.ums.schedule.send.domain.request.upload.TargetUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class SendRequestService {
    private final ScheduleService scheduleService;
    private final SendRequestRepository sendRequestRepository;
    private final Map<String, TargetUploadService> targetUploadServiceMap;

    public SendRequest create(String customerId, SendRequestCommand command, SendRequest sendRequest) {
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
