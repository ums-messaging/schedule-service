package com.ums.schedule.domain.exception.schedule;

import com.ums.schedule.domain.schedule.policy.SchedulePeriod;

import java.time.format.DateTimeFormatter;

public class ScheduleExpiredException extends SchedulePolicyViolationException {

    protected ScheduleExpiredException(String message) {
        super(message);
    }

    public static ScheduleExpiredException of(Long scheduleId, SchedulePeriod schedulePeriod) {
        String startAt = schedulePeriod.getScheduleStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endAt = schedulePeriod.getScheduleEndAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new ScheduleExpiredException("[%d][%s~%s] 이미 만료된 스케쥴 입니다.".formatted(scheduleId, startAt, endAt));
    }
}
