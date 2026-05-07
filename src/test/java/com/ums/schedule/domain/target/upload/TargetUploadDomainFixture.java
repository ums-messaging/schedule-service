package com.ums.schedule.domain.target.upload;

import com.ums.schedule.adapter.api.schedule.ScheduleCreateRequest;
import com.ums.schedule.application.target.dto.TargetDataTransfer;
import com.ums.schedule.code.schedule.CycleCdEnum;
import com.ums.schedule.code.schedule.ScheduleEventEnum;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.code.send.TargetColumnEnum;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.channel.ChannelTemplate;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.cycle_policy.CyclePolicyValue;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.fixture.ScheduleDomainFixture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

public class TargetUploadDomainFixture {

    public static TargetUpload createTargetUpload(TargetUploadTypeEnum targetUploadType){
        Schedule schedule = ScheduleDomainFixture.createSchedule();
        schedule.toStatus(ScheduleEventEnum.TO_RUNNING);
        SendRequest request = SendRequest.of(schedule, null, ChannelTypeEnum.EMAIL);
        return TargetUpload.of(targetUploadType, request);
    }

    public static TargetUpload createTargetUpload(){
        return TargetUploadDomainFixture.createTargetUpload(TargetUploadTypeEnum.JSON);
    }

    public static TargetUpload createTargetUpload(SendRequest sendRequest) {
        return TargetUpload.of(TargetUploadTypeEnum.JSON, sendRequest);
    }


}
