package com.ums.schedule.domain.schedule.cycle_policy;

import com.ums.schedule.code.schedule.CycleCdEnum;
import com.ums.schedule.code.schedule.ScheduleTypeEnum;
import com.ums.schedule.domain.schedule.converter.ScheduleTypeConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}


