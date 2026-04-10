package com.ums.schedule.send.application.model.dto;

import com.ums.schedule.schedule.application.model.dto.ScheduleDto;
import com.ums.schedule.schedule.domain.Schedule;
import com.ums.schedule.send.domain.request.CustomerRequestKey;
import com.ums.schedule.send.domain.request.SendRequest;

public record SendRequestDto(
        Long sendRequestId,
        String customerId,
        String customerRequestId,
        ScheduleDto schedule,
        TargetUploadDto targetUpload,
        String status
) {
    public static SendRequestDto of(SendRequest sendRequest, TargetUploadDto targetUploadDto) {
        CustomerRequestKey customerKey = sendRequest.getCustomerRequestKey();
        Schedule schedule = sendRequest.getSchedule();
        return new SendRequestDto(
                    sendRequest.getId(),
                customerKey.getCustomerId(),
                customerKey.getCustomerRequestId(),
                schedule.toDto(),
                targetUploadDto,
                sendRequest.getStatus().description()
        );
    }
}
