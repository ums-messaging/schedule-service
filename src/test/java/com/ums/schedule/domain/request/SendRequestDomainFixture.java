package com.ums.schedule.domain.request;

import com.ums.schedule.adapter.api.schedule.ScheduleCreateRequest;
import com.ums.schedule.code.schedule.CycleCdEnum;
import com.ums.schedule.code.schedule.ScheduleEventEnum;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.domain.request.CustomerRequestKey;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.cycle_policy.CyclePolicyValue;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.fixture.ScheduleDomainFixture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class SendRequestDomainFixture {
    public static SendRequest createSendRequest() {
        Schedule schedule = ScheduleDomainFixture.createSchedule();
        schedule.toStatus(ScheduleEventEnum.TO_RUNNING);
        return createSendRequest(schedule);
    }
    public static SendRequest createSendRequest(Schedule schedule) {
        CustomerRequestKey key = CustomerRequestKey.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(), false);
        SendRequest request = SendRequest.of(schedule, key, ChannelTypeEnum.EMAIL);
        request.setSenderAndTemplateKey("senderKey", "templateKey");
        request.initRetryMaxCount(3);
        return request;
    }
}
