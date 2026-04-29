package com.ums.schedule.domain.request;

import com.ums.schedule.adapter.api.schedule.ScheduleCreateRequest;
import com.ums.schedule.code.schedule.CycleCdEnum;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.cycle_policy.CyclePolicyValue;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class SendRequestDomainFixture {
    public static SendRequest createSendRequest() {
        Schedule schedule = SendRequestDomainFixture.createSchedule();
        return createSendRequest(schedule);
    }
    public static SendRequest createSendRequest(Schedule schedule) {
        CustomerRequestKey key = CustomerRequestKey.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(), false);
        return SendRequest.of(schedule, key, ChannelTypeEnum.EMAIL);
    }

    public static Schedule createSchedule() {
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
}
