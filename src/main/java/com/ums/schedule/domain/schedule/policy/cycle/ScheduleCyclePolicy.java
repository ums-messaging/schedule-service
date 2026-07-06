package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.domain.schedule.code.CycleCdEnum;
import com.ums.schedule.domain.schedule.code.ScheduleTypeEnum;
import com.ums.schedule.domain.schedule.converter.ScheduleTypeConverter;
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
    private ScheduleTypeEnum scheduleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CycleCdEnum cycleCd;

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
        return new ScheduleCyclePolicy(ScheduleTypeEnum.REALTIME, CycleCdEnum.ALWAYS, null, null);
    }

    public boolean satisfiedCyclePolicy(LocalDateTime requestedAt) {
        if(scheduleType != ScheduleTypeEnum.RESERVATION) {
            return true;
        }

        LocalTime toRequestAt = requestedAt.toLocalTime();
        LocalTime toReservedAt = LocalTime.parse(cycleValue);

        return toRequestAt.isBefore(toReservedAt);
    }
}