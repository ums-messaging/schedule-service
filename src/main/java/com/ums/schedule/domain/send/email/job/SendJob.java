package com.ums.schedule.domain.send.email.job;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.domain.schedule.code.ScheduleTypeEnum;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.policy.cycle.ScheduleCyclePolicy;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;

import java.time.LocalDateTime;

public record SendJob(
        Long jobId,
        Long scheduleId,
        ScheduleTypeEnum scheduleType,
        Long requestId,
        Long reportId,
        String targetUploadId,
        String templateKey,
        Integer retryCount,
        LocalDateTime createdAt
) {

    public static SendJob of(SendRequest sendRequest, Schedule schedule, TargetUploadReport targetUpload) {
        Long id = TsidCreator.getTsid().toLong();
        ScheduleCyclePolicy cyclePolicy = schedule.getCyclePolicy();

        return new SendJob(
                id,
                schedule.getId(),
                cyclePolicy.getScheduleType(),
                sendRequest.getId(),
                null,
                targetUpload.getId().toString(),
                sendRequest.getTemplateKey(),
                sendRequest.getRetryCnt(),
                LocalDateTime.now()
        );
    }
}
