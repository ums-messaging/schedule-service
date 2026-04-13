package com.ums.schedule.application.request.dto;

import com.ums.schedule.application.schedule.dto.ScheduleDto;
import com.ums.schedule.application.target.dto.TargetUploadDto;

public record SendRequestDto(
        Long sendRequestId,
        String customerId,
        String customerRequestId,
        ScheduleDto schedule,
        TargetUploadDto targetUpload,
        String status
) {

}
