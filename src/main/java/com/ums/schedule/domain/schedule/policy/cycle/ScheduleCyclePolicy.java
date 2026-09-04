package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.common.code.schedule.CycleCd;
import com.ums.schedule.common.code.schedule.ScheduleType;
import com.ums.schedule.domain.request.converter.ScheduleTypeConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleCyclePolicy {
    @Convert(converter = ScheduleTypeConverter.class)
    @Column(nullable = false)
    private ScheduleType scheduleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CycleCd cycleCd;

    private String cycleValue;

    @Transient
    private  SchedulePolicyValue policyValue;

    public static ScheduleCyclePolicy of(SchedulePolicyValue policyValue) {
        return new ScheduleCyclePolicy(policyValue.getScheduleType(),
                policyValue.getCycleCdEnum(),
                policyValue.getCycleValue(),
                policyValue);
    }

    public static ScheduleCyclePolicy realtimeOf() {
        return new ScheduleCyclePolicy(ScheduleType.REALTIME, CycleCd.ALWAYS, null, null);
    }

    public boolean satisfiedCyclePolicy(LocalDateTime requestedAt) {
        if(scheduleType != ScheduleType.RESERVATION) {
            return true;
        }

        LocalTime toRequestAt = requestedAt.toLocalTime();
        LocalTime toReservedAt = LocalTime.parse(cycleValue);

        return toRequestAt.isBefore(toReservedAt);
    }
}