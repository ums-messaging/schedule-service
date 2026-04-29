package com.ums.schedule.domain.target.upload;

import com.ums.schedule.adapter.api.schedule.ScheduleCreateRequest;
import com.ums.schedule.application.target.dto.TargetDataTransfer;
import com.ums.schedule.code.schedule.CycleCdEnum;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

public class TargetUploadDomainFixture {
    private static Schedule createSchedule() {
        LocalDateTime startDt = LocalDateTime.now();
        LocalDateTime endDt = LocalDateTime.now().plusMonths(1);

        SchedulePeriod period = SchedulePeriod.of(startDt, endDt);
        ScheduleCyclePolicy cyclePolicy = ScheduleCyclePolicy.of(CyclePolicyValue.of(CycleCdEnum.ALWAYS, 0));
        ScheduleCreateRequest request = new ScheduleCreateRequest("schedule", "REALTIME", null, null,
                startDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                endDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        Schedule schedule = Schedule.of(request.scheduleName(), period, cyclePolicy);
        schedule.toRunning();
        return schedule;
    }

    public static TargetUpload createTargetUpload(TargetUploadTypeEnum targetUploadType){
        Schedule schedule = TargetUploadDomainFixture.createSchedule();
        SendRequest request = SendRequest.of(schedule, null, ChannelTypeEnum.EMAIL);
        return TargetUpload.of(targetUploadType, request);
    }

    public static TargetUpload createTargetUpload(){
        return TargetUploadDomainFixture.createTargetUpload(TargetUploadTypeEnum.JSON);
    }

    public static ChannelTemplate createTemplate() {
        return new FakeTemplate();
    }

    public static FakeSendTarget createTargetDto() {
        return new FakeSendTarget();
    }

    static class FakeTemplate implements ChannelTemplate {

        @Override
        public String compile(SendTarget target) {
            return null;
        }

        @Override
        public String getTitle(SendTarget target) {
            return null;
        }
    }

    static class FakeSendTarget implements TargetDataTransfer {

        @Override
        public Map<String, Object> extractMessageVariable() {
            return Map.of("serial_no", UUID.randomUUID().toString());
        }

        @Override
        public Map<TargetColumnEnum, String> resolveTargetData() {
            return Map.of(
                    TargetColumnEnum.TARGET_KEY, UUID.randomUUID().toString(),
                    TargetColumnEnum.TARGET_EMAIL, "jang314@naver.com",
                    TargetColumnEnum.TARGET_NAME, "jang"
            );
        }
    }
}
