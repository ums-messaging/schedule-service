package com.ums.schedule.application.ums.common.request;

import com.ums.schedule.application.schedule.exception.ScheduleNotFoundException;
import com.ums.schedule.application.target.report.TargetUploadReportCreateService;
import com.ums.schedule.application.sendrequest.command.SendRequestCreateCommand;
import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.application.ums.common.request.model.SendRequestCreateContext;
import com.ums.schedule.application.ums.common.request.model.SendRequestCreateResult;
import com.ums.schedule.application.target.report.model.TargetUploadResult;
import com.ums.schedule.common.config.SendRequestProperties;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import com.ums.schedule.domain.request.customer.CustomerRequestKey;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleJpaRepository;
import com.ums.schedule.domain.message.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendRequestCreateService {
    private final SendRequestProperties properties;
    private final ScheduleJpaRepository scheduleRepository;
    private final SendRequestRepository sendRequestRepository;
    private final TargetUploadReportCreateService targetUploadService;

    @Transactional
    public SendRequestCreateResult create(SendRequestCreateCommand command, SendMessage message) {
        Schedule schedule = scheduleRepository.findById(command.scheduleId())
                .orElseThrow(() -> ScheduleNotFoundException.of(command.scheduleId()));

        CustomerRequestKey customerRequestKey = createCustomerRequestKey(command.customerId(), command.customerKey());
        Integer retryCount = getOrDefaultRetryCount(command.retryCnt());

        SendRequestCreateContext context = command.toContext(schedule, customerRequestKey, message, retryCount);
        SendRequest sendRequest = SendRequest.of(context);

        TargetUploadCreateCommand uploadCommand = TargetUploadCreateCommand.of(sendRequest, command);
        TargetUploadResult result = targetUploadService.create(sendRequest, uploadCommand);

        sendRequestRepository.save(sendRequest);

        return SendRequestCreateResult.of(message.getId(), sendRequest, result);
    }

    private Integer getOrDefaultRetryCount(Integer retryCount) {
        Integer getRetryCount = getRetryCount(retryCount);
        return getRetryCount == 0 ? getConfiguredRetryCount() : retryCount;
    }

    private Integer getConfiguredRetryCount() {
        Integer getRetryCount = properties.getRetryCount();
        return getRetryCount == null ? -1 : getRetryCount;
    }

    private Integer getRetryCount(Integer retryCount) {
        return retryCount == null ? 0 : retryCount;
    }

    private CustomerRequestKey createCustomerRequestKey(String customerId, String customerRequestKey) {
        CustomerRequestKey key = CustomerRequestKey.of(customerId, customerRequestKey);
        if(sendRequestRepository.existsByCustomerRequestKey(key)) {
            return null;
        }
        return key;
    }
}
